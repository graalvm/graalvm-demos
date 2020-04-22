// Copyright (c) 2017, 2019, Oracle and/or its affiliates. All rights reserved.

variable "tenancy_ocid" {}
variable "user_ocid" {}
variable "fingerprint" {}
variable "private_key_path" {}
variable "region" {}
variable "compartment_ocid" {}
variable "ssh_public_key" {}
variable "ssh_private_key" {}

provider "oci" {
  tenancy_ocid     = "${var.tenancy_ocid}"
  user_ocid        = "${var.user_ocid}"
  fingerprint      = "${var.fingerprint}"
  private_key_path = "${var.private_key_path}"
  region           = "${var.region}"
}

// TODO: change this to some representative name. Lowercase only no special characters, no more than 5 chars eg: test
variable "sufix_name" {
  default = "test"
}

// TODO: change if +/- instances
variable "num_instances" {
  default = "2"
}


variable "instance_shape" {
  default = "VM.Standard1.2"
}

variable "instance_image_ocid" {
  type = "map"

  default = {
    # See https://docs.us-phoenix-1.oraclecloud.com/images/
    # Oracle-provided image "Oracle-Linux-7.5-2018.10.16-0"
    us-phoenix-1 = "ocid1.image.oc1.phx.aaaaaaaaoqj42sokaoh42l76wsyhn3k2beuntrh5maj3gmgmzeyr55zzrwwa"
    us-ashburn-1   = "ocid1.image.oc1.iad.aaaaaaaageeenzyuxgia726xur4ztaoxbxyjlxogdhreu3ngfj2gji3bayda"
    eu-frankfurt-1 = "ocid1.image.oc1.eu-frankfurt-1.aaaaaaaaitzn6tdyjer7jl34h2ujz74jwy5nkbukbh55ekp6oyzwrtfa4zma"
    uk-london-1    = "ocid1.image.oc1.uk-london-1.aaaaaaaa32voyikkkzfxyo4xbdmadc2dmvorfxxgdhpnk6dw64fa3l4jh7wa"
  }
}

data "oci_identity_availability_domain" "ad" {
  compartment_id = "${var.tenancy_ocid}"
  ad_number      = 1
}

resource "oci_core_vcn" "graalvm_vcn" {
  cidr_block     = "10.1.0.0/16"
  compartment_id = "${var.compartment_ocid}"
  display_name   = "GraalVMVcn-${var.sufix_name}"
  dns_label      = "graalvmvcn${var.sufix_name}"
}

resource "oci_core_security_list" "graalvm_hosts_security_list" {
  compartment_id = "${var.compartment_ocid}"
  vcn_id         = "${oci_core_vcn.graalvm_vcn.id}"
  display_name   = "GraalvmHostsSecurityLists-${var.sufix_name}"

  // allow outbound tcp traffic on all ports
  egress_security_rules {
    destination = "0.0.0.0/0"
    protocol    = "all"
    stateless = false
  }

  // allow inbound ssh traffic from a specific port
  ingress_security_rules {
    protocol  = "6"         // tcp
    source    = "0.0.0.0/0"
    stateless = false

    tcp_options {
      // These values correspond to the destination port range.
      min = 22
      max = 22
    }
  }

  // allow inbound icmp traffic of a specific type
  ingress_security_rules {
    protocol  = 1
    source    = "0.0.0.0/0"
    stateless = true

    icmp_options {
      type = 3
      code = 4
    }
  }

  // allow inbound http traffic on specific ports 8080-8085
  ingress_security_rules {
    protocol  = "6"         // tcp
    source    = "10.1.0.0/16"
    stateless = false

    tcp_options {
      // These values correspond to the destination port range.
      min = 8080
      max = 8085
    }
  }

  // allow inbound http traffic on specific ports 8080-8085
  ingress_security_rules {
    protocol  = "6"         // tcp
    source    = "10.1.0.0/16"
    stateless = false

    tcp_options {
      // These values correspond to the destination port range.
      min = 8443
      max = 8443
    }
  }
}

resource "oci_core_internet_gateway" "graalvm_internet_gateway" {
  compartment_id = "${var.compartment_ocid}"
  display_name   = "GraalVMInternetGateway-${var.sufix_name}"
  vcn_id         = "${oci_core_vcn.graalvm_vcn.id}"
}

resource "oci_core_route_table" "graalvm_route_table" {
  compartment_id = "${var.compartment_ocid}"
  vcn_id         = "${oci_core_vcn.graalvm_vcn.id}"
  display_name   = "GraalVMRouteTable-${var.sufix_name}"

  route_rules {
    destination       = "0.0.0.0/0"
    destination_type  = "CIDR_BLOCK"
    network_entity_id = "${oci_core_internet_gateway.graalvm_internet_gateway.id}"
  }
}

resource "oci_core_subnet" "graalvm_subnet" {
  availability_domain = "${data.oci_identity_availability_domain.ad.name}"
  cidr_block          = "10.1.20.0/24"
  display_name        = "graalvmSubnet-${var.sufix_name}"
  dns_label           = "graalvmsubnet"
  security_list_ids   = ["${oci_core_security_list.graalvm_hosts_security_list.id}"]
  compartment_id      = "${var.compartment_ocid}"
  vcn_id              = "${oci_core_vcn.graalvm_vcn.id}"
  route_table_id      = "${oci_core_route_table.graalvm_route_table.id}"
  dhcp_options_id     = "${oci_core_vcn.graalvm_vcn.default_dhcp_options_id}"
}


resource "oci_core_instance" "graalvm_test_instance" {
  availability_domain = "${data.oci_identity_availability_domain.ad.name}"
  compartment_id      = "${var.compartment_ocid}"
  display_name        = "GraalVMHost-${var.sufix_name}"
  shape               = "${var.instance_shape}"
  count = "${var.num_instances}"

  create_vnic_details {
    subnet_id        = "${oci_core_subnet.graalvm_subnet.id}"
    display_name     = "HostPrimaryVnic-${var.sufix_name}"
    assign_public_ip = true
    hostname_label   = "GraalVMHost-${var.sufix_name}-${count.index}"
  }

  metadata = {
    ssh_authorized_keys = "${var.ssh_public_key}"
    user_data           = "${base64encode(file("./init.sh"))}"
  }

  source_details {
    source_type = "image"
    source_id   = "${var.instance_image_ocid[var.region]}"
  }

  timeouts {
    create = "60m"
  }
}

resource "null_resource" "remote-exec" {
  depends_on = ["oci_core_instance.graalvm_test_instance"]
  count = "${var.num_instances}"

  provisioner "remote-exec" {
    connection {
      agent = false
      timeout = "30m"
      host = "${oci_core_instance.graalvm_test_instance.*.public_ip[count.index % var.num_instances]}"
      user = "opc"
      private_key = "${var.ssh_private_key}"
    }

    script = "provision.sh"
  }
}

output "instance_private_ips" {
  value = ["${oci_core_instance.graalvm_test_instance.*.private_ip}"]
}

output "instance_public_ips" {
  value = ["${oci_core_instance.graalvm_test_instance.*.public_ip}"]
}

