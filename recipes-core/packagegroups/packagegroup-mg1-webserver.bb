DESCRIPTION = "MG1 Web Server Package Group"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-mg1-webserver \
"

RDEPENDS:${PN} = "\
    nginx \
" 