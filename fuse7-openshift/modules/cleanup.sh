echo
echo "########################################################################"
echo "Cleanup Techlab Hospital demonstration..."
echo "########################################################################"
echo

oc delete project techlab-hospital-prod
oc delete project techlab-hospital
oc delete bc event-bus-pipeline -n fabric
