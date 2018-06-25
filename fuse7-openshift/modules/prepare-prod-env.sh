echo
echo "########################################################################"
echo "Preparing Techlab Hospital production environment..."
echo "########################################################################"
echo

# Create prod project
oc new-project techlab-hospital-prod --display-name="Techlab Hospital (PROD)"

# Adjust project permissions
oc adm policy add-role-to-user edit system:serviceaccount:fabric:jenkins -n techlab-hospital
oc adm policy add-role-to-user edit system:serviceaccount:fabric:jenkins -n techlab-hospital-prod

# Allow test and prod to pull from dev
oc adm policy add-role-to-group system:image-puller system:serviceaccounts:techlab-hospital-prod -n techlab-hospital

# Deploy AMQ broker
oc new-app --template=amq63-persistent --name=amq63-persistent \
  --param=APPLICATION_NAME=broker \
  --param=MQ_PROTOCOL=openwire,amqp \
  --param=MQ_USERNAME=admin \
  --param=MQ_PASSWORD=admin \
  --param=AMQ_STORAGE_USAGE_LIMIT=10gb \
  --param=AMQ_QUEUE_MEMORY_LIMIT=1gb

# After having created development bc, dc, svc, routes
oc create deploymentconfig api-gateway --image=docker-registry.default.svc:5000/techlab-hospital/api-gateway:promoteToProd -n techlab-hospital-prod
oc set env dc/api-gateway SPRING_APPLICATION_JSON={"server":{"undertow":{"io-threads":1, "worker-threads":2 }}} JAEGER_SERVER_HOSTNAME=jaeger-agent.cockpit.svc.cluster.local AB_JMX_EXPORTER_CONFIG=prometheus-config.yml KUBERNETES_NAMESPACE=techlab-hospital-prod -n techlab-hospital-prod

oc create deploymentconfig administration --image=docker-registry.default.svc:5000/techlab-hospital/administration:promoteToProd -n techlab-hospital-prod
oc set env dc/administration AMQP_HOST=broker-amq-amqp SPRING_APPLICATION_JSON={"server":{"undertow":{"io-threads":1, "worker-threads":2 }}} JAEGER_SERVER_HOSTNAME=jaeger-agent.cockpit.svc.cluster.local KUBERNETES_NAMESPACE=techlab-hospital-prod -n techlab-hospital-prod

oc create deploymentconfig uds-chirabdo --image=docker-registry.default.svc:5000/techlab-hospital/uds-chirabdo:promoteToProd -n techlab-hospital-prod
oc set env dc/uds-chirabdo AMQP_HOST=broker-amq-amqp SPRING_APPLICATION_JSON={"server":{"undertow":{"io-threads":1, "worker-threads":2 }}} JAEGER_SERVER_HOSTNAME=jaeger-agent.cockpit.svc.cluster.local KUBERNETES_NAMESPACE=techlab-hospital-prod -n techlab-hospital-prod

oc create deploymentconfig uds-chirortho --image=docker-registry.default.svc:5000/techlab-hospital/uds-chirortho:promoteToProd -n techlab-hospital-prod
oc set env dc/uds-chirortho AMQP_HOST=broker-amq-amqp SPRING_APPLICATION_JSON={"server":{"undertow":{"io-threads":1, "worker-threads":2 }}} JAEGER_SERVER_HOSTNAME=jaeger-agent.cockpit.svc.cluster.local KUBERNETES_NAMESPACE=techlab-hospital-prod -n techlab-hospital-prod

oc create deploymentconfig imagerie --image=docker-registry.default.svc:5000/techlab-hospital/imagerie:promoteToProd -n techlab-hospital-prod
oc set env dc/imagerie AMQP_HOST=broker-amq-amqp SPRING_APPLICATION_JSON={"server":{"undertow":{"io-threads":1, "worker-threads":2 }}} JAEGER_SERVER_HOSTNAME=jaeger-agent.cockpit.svc.cluster.local KUBERNETES_NAMESPACE=techlab-hospital-prod -n techlab-hospital-prod

oc create deploymentconfig laboratoire --image=docker-registry.default.svc:5000/techlab-hospital/laboratoire:promoteToProd -n techlab-hospital-prod
oc set env dc/laboratoire AMQP_HOST=broker-amq-amqp SPRING_APPLICATION_JSON={"server":{"undertow":{"io-threads":1, "worker-threads":2 }}} JAEGER_SERVER_HOSTNAME=jaeger-agent.cockpit.svc.cluster.local KUBERNETES_NAMESPACE=techlab-hospital-prod -n techlab-hospital-prod

oc create deploymentconfig event-bus --image=docker-registry.default.svc:5000/techlab-hospital/event-bus:promoteToProd -n techlab-hospital-prod
oc set env dc/event-bus AMQP_HOST=broker-amq-amqp SPRING_APPLICATION_JSON={"server":{"undertow":{"io-threads":1, "worker-threads":2 }}} JAEGER_SERVER_HOSTNAME=jaeger-agent.cockpit.svc.cluster.local KUBERNETES_NAMESPACE=techlab-hospital-prod -n techlab-hospital-prod

oc rollout cancel dc/api-gateway -n techlab-hospital-prod
oc rollout cancel dc/administration -n techlab-hospital-prod
oc rollout cancel dc/uds-chirabdo -n techlab-hospital-prod
oc rollout cancel dc/uds-chirortho -n techlab-hospital-prod
oc rollout cancel dc/imagerie -n techlab-hospital-prod
oc rollout cancel dc/laboratoire -n techlab-hospital-prod
oc rollout cancel dc/event-bus -n techlab-hospital-prod

oc get dc/api-gateway -o json -n techlab-hospital-prod | jq '.spec.triggers |= []' | oc replace -f -
oc get dc/administration -o json -n techlab-hospital-prod | jq '.spec.triggers |= []' | oc replace -f -
oc get dc/uds-chirabdo -o json -n techlab-hospital-prod | jq '.spec.triggers |= []' | oc replace -f -
oc get dc/uds-chirortho -o json -n techlab-hospital-prod | jq '.spec.triggers |= []' | oc replace -f -
oc get dc/imagerie -o json -n techlab-hospital-prod | jq '.spec.triggers |= []' | oc replace -f -
oc get dc/laboratoire -o json -n techlab-hospital-prod | jq '.spec.triggers |= []' | oc replace -f -
oc get dc/event-bus -o json -n techlab-hospital-prod | jq '.spec.triggers |= []' | oc replace -f -

oc get dc api-gateway -o yaml -n techlab-hospital-prod | sed 's/imagePullPolicy: IfNotPresent/imagePullPolicy: Always/g' | oc replace -f -
oc get dc administration -o yaml -n techlab-hospital-prod | sed 's/imagePullPolicy: IfNotPresent/imagePullPolicy: Always/g' | oc replace -f -
oc get dc uds-chirabdo -o yaml -n techlab-hospital-prod | sed 's/imagePullPolicy: IfNotPresent/imagePullPolicy: Always/g' | oc replace -f -
oc get dc uds-chirortho -o yaml -n techlab-hospital-prod | sed 's/imagePullPolicy: IfNotPresent/imagePullPolicy: Always/g' | oc replace -f -
oc get dc laboratoire -o yaml -n techlab-hospital-prod | sed 's/imagePullPolicy: IfNotPresent/imagePullPolicy: Always/g' | oc replace -f -
oc get dc imagerie -o yaml -n techlab-hospital-prod | sed 's/imagePullPolicy: IfNotPresent/imagePullPolicy: Always/g' | oc replace -f -
oc get dc event-bus -o yaml -n techlab-hospital-prod | sed 's/imagePullPolicy: IfNotPresent/imagePullPolicy: Always/g' | oc replace -f -

oc expose dc api-gateway --port=8080 --target-port=8080 -n techlab-hospital-prod
oc expose dc administration --port=8080 --target-port=8080 -n techlab-hospital-prod
oc expose dc uds-chirabdo --port=8080 --target-port=8080 -n techlab-hospital-prod
oc expose dc uds-chirortho --port=8080 --target-port=8080 -n techlab-hospital-prod
oc expose dc laboratoire --port=8080 --target-port=8080 -n techlab-hospital-prod
oc expose dc imagerie --port=8080 --target-port=8080 -n techlab-hospital-prod
oc expose dc event-bus --port=8080 --target-port=8080 -n techlab-hospital-prod
