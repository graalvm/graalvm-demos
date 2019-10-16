#Infrastructure

These scripts give the ability to provision and manage compute capacity using [Oracle Cloud Infrastructure](https://docs.cloud.oracle.com/iaas/Content/services.htm), in
order to deploy the services shown in this examples. In summary this scripts create computes instances and associated 
network resources for those instances. It creates a virtual network with a subnet and puts 2 hosts in such subnet.
It then adds the necessary permissions to allow services to communicate in the subnet within specific ports

Finally it runs init scripts to install basic tool in the hosts

## Pre-requisites

- A properly configured [Oracle Cloud Infrastructure account](https://docs.cloud.oracle.com/iaas/Content/API/Concepts/apisigningkey.htm)
- [Terraform](https://learn.hashicorp.com/terraform/getting-started/install.html) 
  - try `brew install terraform`
- Create a credentials file called `credentials.rc` for your OCI account. The file should look like the below.
```
##Terraform env variables
export TF_VAR_tenancy_ocid=[TENANCY_OCID]
export TF_VAR_compartment_ocid=[COMPARMENT_OCID]
export TF_VAR_user_ocid=[USER_OCID]
export TF_VAR_fingerprint=[FINGERPRINT]
export TF_VAR_private_key_path=[PATH_TO_YOUR_ACCOUNT_PRIVATE_KEY eg: ~/.oci/key.pem]
export TF_VAR_region=[REGION NAME eg: us-ashburn-1]

## ssh keys that will be used for remote access authenication
export TF_VAR_ssh_public_key="$(cat [PATH_TO_SSH_PUBLIC_KEY])"
export TF_VAR_ssh_private_key="$(cat [PATH_TO_SSH_PRIVATE_KEY])"
```
Notice the `TF_VAR` prefix, keep it so as it is a terraform convention for input variables. More information [here](https://www.terraform.io/docs/providers/oci/index.html)

## Provisioning Infrastructure
- Sources your credentials file

```$> source credentials.rc```

### Create infrastructure

- Deploy with terraform

```$> terraform init```
```$> terraform apply --auto-approve```

The deployment process should end with a list of private/public ip addresses like so
```
Apply complete! Resources: 9 added, 0 changed, 0 destroyed.

Outputs:

instance_private_ips = [
    10.1.20.2,
    10.1.20.3
]
instance_public_ips = [
    1xx.145.174.85,
    1xx.145.208.127
]

```

## Deploy your software

- use docker for it
```$> docker save [DOCKER_IMAGE]|pv|ssh [PUBLIC_IP_ADDRESS] "sudo docker load"```


### Destroy infrastructure

- Remove with terraform

```$> terraform destroy --auto-approve```

For more information on terraform infrastructure management see: [Terraform for OCI](https://www.terraform.io/docs/providers/oci/index.html)
and  more [terraform commands](https://www.terraform.io/docs/commands/index.html)




