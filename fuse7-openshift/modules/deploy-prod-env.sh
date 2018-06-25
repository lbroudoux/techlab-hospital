echo
echo "########################################################################"
echo "Deploying Techlab Hospital production environment..."
echo "########################################################################"
echo

# Tagging images from dev project
oc project techlab-hospital-prod

oc tag techlab-hospital/api-gateway:latest techlab-hospital/api-gateway:promoteToProd
oc tag techlab-hospital/administration:latest techlab-hospital/administration:promoteToProd
oc tag techlab-hospital/uds-chirabdo:latest techlab-hospital/uds-chirabdo:promoteToProd
oc tag techlab-hospital/uds-chirortho:latest techlab-hospital/uds-chirortho:promoteToProd
oc tag techlab-hospital/laboratoire:latest techlab-hospital/laboratoire:promoteToProd
oc tag techlab-hospital/imagerie:latest techlab-hospital/imagerie:promoteToProd
oc tag techlab-hospital/event-bus:latest techlab-hospital/event-bus:promoteToProd

oc rollout latest dc/api-gateway -n techlab-hospital-prod
oc rollout latest dc/administration -n techlab-hospital-prod
oc rollout latest dc/uds-chirabdo -n techlab-hospital-prod
oc rollout latest dc/uds-chirortho -n techlab-hospital-prod
oc rollout latest dc/laboratoire -n techlab-hospital-prod
oc rollout latest dc/imagerie -n techlab-hospital-prod
oc rollout latest dc/event-bus -n techlab-hospital-prod

oc create -f pipeline.yml -n fabric
