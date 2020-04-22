#!/bin/bash

#Open ports for the services to communicate
sudo firewall-cmd --zone=public --permanent --add-port=8080-8085/tcp
sudo firewall-cmd --zone=public --permanent --add-port=8443/tcp
sudo firewall-cmd --reload



