# MG1 Environment Image - Universal Feature Extension
# This image can work with any base configuration

DESCRIPTION = "MG1 Environment Image - Web Server Features"

# 基礎映像 - 可以根據需要調整
require recipes-core/images/core-image-minimal.bb

IMAGE_FEATURES += "ssh-server-dropbear"

# 基礎套件
IMAGE_INSTALL += " \
	packagegroup-core-base-utils \
	iproute2 \
"

# 開發工具
IMAGE_INSTALL += "\
	python3-pip \
	opkg \
"

# MG1 環境 - Web Server 功能
EXTRA_IMAGE_FEATURES += " package-management ssh-server-openssh"
IMAGE_ROOTFS_EXTRA_SPACE = "5000000"

IMAGE_INSTALL += " \
    nginx \
    ntpdate \
"

# 配置 nginx 模組
PACKAGECONFIG:append:pn-nginx = " stream http-auth-request gunzip"

# fix resolv.conf
ROOTFS_POSTPROCESS_COMMAND += "fix_resolv_conf_symlink;"

fix_resolv_conf_symlink () {
    # remove wrong symbolic link
    rm -f ${IMAGE_ROOTFS}/etc/resolv.conf

    # if NetworkManager:resolv.conf is exist
    if [ -e ${IMAGE_ROOTFS}/run/NetworkManager/resolv.conf ]; then
        ln -s /run/NetworkManager/resolv.conf ${IMAGE_ROOTFS}/etc/resolv.conf
    else
        # try systemd-resolved function
        ln -s /run/systemd/resolve/stub-resolv.conf ${IMAGE_ROOTFS}/etc/resolv.conf
    fi
} 