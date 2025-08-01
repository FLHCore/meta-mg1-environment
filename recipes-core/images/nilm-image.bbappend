# Add MG1 web server features to NILM image
IMAGE_INSTALL += "packagegroup-mg1-webserver"

# Configure nginx modules
PACKAGECONFIG:append:pn-nginx = " stream http-auth-request gunzip" 