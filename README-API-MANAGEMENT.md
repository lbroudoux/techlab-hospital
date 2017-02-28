# techlab-hospital with API Management

In addition of the base demonstration of [Red Hat JBoss Fuse solution](https://developers.redhat.com/products/fuse/overview/) deployed on [OpenShift](https://www.openshift.org), it is also possible to deploy the [3scale API Management solution](https://www.3scale.com) on that same platform in order to exposed in a controled fashion the Hospital API to the rest of the world!

## Architecture overview

The demonstration is built upon the base Techlab Hospital demonstration and is adding an extra-module on OpenShift : a 3scale Gateway configured to protected the REST API from `api-gateway` and the WebSocket API from `event-bus` technical modules.

![overview](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/assets/architecture-api-management.png)  

For configuring the API Management policies, you should have access to a 3scale API Management System that is a SaaS offering from Red Hat for now. You should be able to install such a API Management System on-premise on OpenShift at the end of H1 2017 (beta program is on its way!).

### Console overview

For testing/demonstration purpose, we have introduced a 2nd web console that has the same features as the first one but that is using API Endpoints managed by 3scale solution.

![overview](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/assets/console-api-management-screenshot.png)  

Register a patient involves the admission form on the left. Following/tracking business events is done through the panel on the right where events appears as process flows. A new button `Refresh` appears here in order to call the API many-many-many times and demonstrate the throttling features on the API Gateway.

## Installation

### Pre-requisites

It is assumed that you have first fully installed the Techlab Hospital demonstration. So check [README](https://github.com/lbroudoux/techlab-hospital/blob/master/README.md) in order to going through pre-requises and modules installation.

For the `API Management System` you should have a valid 3scale account. For the `API Management Gateway` module, you need an OpenShift Container Platform cluster instance running and available.

_Note: the Docker image references from within this template is only present into Red Hat Docker registry. It is not (yet?) distributed through Docker Hub. So you should have a valid OCP instance running, an OpenShift Origin instance will not have direct access toe Red Hat registry !_

Once your OpenShift instance is up and running, ensure you've got the Red Hat 3scale Gateway template installed onto OpenShift (it comes bundled by default with OpenShift Container Platform 3.4). You can check this going to the catalogue view of OpenShift web console. If not present, you can run the following command for installing missing image streams and templates :

    oc create -f https://raw.githubusercontent.com/3scale/3scale-amp-openshift-templates/master/apicast-gateway/apicast-gateway-template.yml -n openshift

### Configuring API on 3scale

You will have to create/declare 2 API to manage within your account :
* `TechLab Hospital API` is the managed API corresponding to the REST Endpoint offered by `api-gateway` module,
* `TechLab Hospital Event API` is the managed API corresponding to the WebSocket Endpoint offered by `event-bus` module.

You may need to create Application Plans for this APIs and then define Mapping and Integration rules for them. The screenshots below show you the definitions of custom methods that will allow to later trace the consumption of APIs through analytics

![api rules](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/assets/3scale-api-rules.png)

![api public](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/assets/3scale-api-public.png)

![event rules](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/assets/3scale-event-rules.png)

![event public](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/assets/3scale-event-public.png)

Please note that the `Public Base URL` mentioned above are the URL of the Routes we'll have to create later on OpenShift. Note also that the `Private Base URL` aforedmentioned are directly the OpenShift Services of our Techlab Hospital modules. Thus 3scale Gateway may also benefits from service discovery present into Kuberntes !

### Installing 3scale API Gateway on OpenShift

Because the API Gateway will connect to remote API Management System, the first thing to do is to ensure that OpenShift will have an `Access Token` and that this token will be available as a secret within project. Within the 3scale Management console, go to your `Personal settings` and retrieve or create a token like shown below :

![access token](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/assets/3scale-access-token.png)  

Once you have retrieved that token, login onto your OpenShift cluster using `oc` tool, go to your project and apply following command for creating a secret :

    oc secret new-basicauth threescale-portal-endpoint-secret --password=https://<my_3scale_access_token_for_openshift>@<my_3scale_admin_domain>

You can now use the regular catalog for adding a new `3scale gateway` component. The `APICAST_SERVICES` parameter is the most important one and should absolutely be customized to the identifiers APIs you previsouly created in 3scale Management System. This ID is find on the summary of API. The two IDs should be appended using a comma like that: `2555417739331,2555417739332`.

In order to have a better control on what is deployed, you can set the following template parameters :
* `APICAST_LOG_LEVEL`: `info`,
* `REQUEST_LOGS`: `true`,
* `RESPONSE_CODES`: `true`

Deployment should now start for a few minutes. Last step is now to declare Routes for the 2 managed endpoints you setup. Basically, you should be able to do this with the following commands (in respect of the route names in the above captures) :

    oc expose service/hospital-api --name hospital-api
    oc expose service/hospital-api --name hospital-event

### Demonstrating

Once all modules have been deployed onto your OpenShift instance, you can easily demonstrate the patient admission/registration process using the provided console.

Just open the `/console/index-3scale.html` file within a browser. When running locally, IP of OpenShift instance and 3scale authentication user key should be configured. This can be easily done using the inputs into header bar of the console.

You should now be able to demonstrate how console interacts with API, even showing authorization errors when request quotas is reached for user. The analytics section on 3scale API Management System should be updated in a near real-time fashion, allowing you to see nice charts like this one :

![3scale analytics](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/assets/3scale-analytics.png)

Happy testing !
