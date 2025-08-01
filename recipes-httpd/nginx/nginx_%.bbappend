FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Add custom nginx configuration
SRC_URI += "file://mg1-nginx.conf"

# Install custom configuration
do_install:append() {
    install -d ${D}${sysconfdir}/nginx/conf.d
    install -m 0644 ${WORKDIR}/mg1-nginx.conf ${D}${sysconfdir}/nginx/conf.d/
}

# 指定使用 nginx 1.24.0 版本
PREFERRED_VERSION_nginx = "1.24.0"

# 覆蓋 DEFAULT_PREFERENCE，讓 1.24.0 有更高優先級
DEFAULT_PREFERENCE = "1" 