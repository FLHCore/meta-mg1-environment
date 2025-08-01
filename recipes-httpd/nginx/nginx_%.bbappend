FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Add custom nginx configuration
SRC_URI += "file://mg1-nginx.conf"

# Install custom configuration
do_install:append() {
    install -d ${D}${sysconfdir}/nginx/conf.d
    install -m 0644 ${WORKDIR}/mg1-nginx.conf ${D}${sysconfdir}/nginx/conf.d/
} 