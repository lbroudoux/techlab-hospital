
oc secret new-basicauth threescale-portal-endpoint-secret --password=https://1a56231afd41974e61428922460c493575b756f63b1bb5d306f561e4a1c12ab1@lbroudou-redhat-admin.3scale.net
oc secret new-basicauth apicast-configuration-url-secret --password=https://1a56231afd41974e61428922460c493575b756f63b1bb5d306f561e4a1c12ab1@lbroudou-redhat-admin.3scale.net

oc new-app 3scale-gateway \
  --param=APICAST_NAME=hospital-api \
  --param=LOG_LEVEL=info \
  --param=RESPONSE_CODES=true

oc expose service/hospital-api --name hospital-api
