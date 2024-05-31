# Digital Ocean Droplet Ubuntu setup

# 0. install some utilities
sudo apt install net-tools


# 1. Change default editor to vim:
sudo update-alternatives --config editor

# 2. Modify useradd to use /bin/bash as default shell
vi /etc/default/useradd

# 3. Add/Create admin user
useradd -m cyberadmin
usermod -aG admin cyberadmin

# 4. Enable passwordless sudo for admin
visudo
%admin ALL=(ALL) NOPASSWD: ALL


# 5. Transfer public key to remote admin authorizedkeys
sudo su cyberadmin
cd
mkdir .ssh
cd .ssh
vi authorizedkeys
chmod 600 authorizedkeys
cd ../
chmod 700 .ssh

# 6. Change default sshd port on server from 22 to 3865
vi /etc/ssh/sshd_config

# 6a. Additional steps for ubuntu 22.10-24.04LTS
sudo vi /lib/systemd/system/ssh.socket 
sudo systemctl daemon-reload
sudo systemctl restart ssh

# 7. Configure and enable ufw to allow connections on 3865
