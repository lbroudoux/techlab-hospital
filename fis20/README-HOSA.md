## Getting application metrics with HOSA

Hawkular OpenShit Agent can be used with Fuse Integration Services 2.0 applications to gather and later retrieve application metrics exposed with Jolokia. Promotheus endpoint should also be included into a near-future so that you'll be able to easily add any custom metrics.

Here's a sample of a custom dashboard using Grafana on top of HOSA and displaying `ThreadCount` and `HeapMemoryUsage` metrics provided by the `api-gateway` module of our use-case:

![dashboard](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/assets/hosa-dashboard.png)

This README is actually a summary and a complement of this excellent blog article [introducing HOSA](http://www.hawkular.org/blog/2017/01/17/obst-hosa.html).


### Installation of HOSA

Templates used for installation of the agent are found within the `/hosa` sub-directory of the FIS 2.0 implementation. In the `default` project of your OpenShift cluster, run the following commands:

    oc create -f hawkular-openshift-agent-configmap.yaml -n default
    oc process -f hawkular-openshift-agent.yaml | oc create -n default -f -
    oc adm policy add-cluster-role-to-user hawkular-openshift-agent system:serviceaccount:default:hawkular-openshift-agent

You should get an `hawkular-openshift-agent-xxxxx` pod running in minutes.

### Allow connection of Agent to Jolokia running into project pods

 Create a secret into the `techlab-hospital` so that our pods will use the contained password to secure Jolika endpoint. Secret will also be shared to `hawkular-openshift-agent` because used within the `ConfigMap`object exposed by each pod declaring custom metrics.

    oc create -f hosa-secret.yml -n techlab-hospital

### Install and configure Grafana

Installation of Grafana could be done into a shared project but for maximum flexibility, we'll install it into our own `techlab-hospital` project. It's just a matter of running this command:

    oc new-app docker.io/hawkular/hawkular-grafana-datasource -n techlab-hospital


### Create Service Account for connection to hawkular-metrics

For configuring the `Hawkular` datasource into Grafana, we will need a permanent token corresponding to a user authorized to access the `hawkular-metrics` service and read cluster metrics. This can be easily done creating a Service Account that way:

    oc create sa view-metrics -n <techlab-project>

```sh
$ oc describe sa/view-metrics -n <techlab-project>
Name:		view-metrics
Namespace:	techlab-hospital
Labels:		<none>

Image pull secrets:	view-metrics-dockercfg-rmnee

Mountable secrets: 	view-metrics-dockercfg-rmnee
                 	  view-metrics-token-vowtw

Tokens:            	view-metrics-token-t98qw
                 	  view-metrics-token-vowtw
```
The Token that is not shared with moutable secrets can then be used as a permament token but before that, we have to authorize the Service Account to access cluster metrics by having the `cluster-reader` role :

    oc adm policy add-cluster-role-to-user cluster-reader system:serviceaccount:<techlab-project>:view-metrics

Now we can retrieve the token and use it as a token within Grafana datasource edit panel:

```sh
$ oc describe secret view-metrics-token-t98qw
Name:		view-metrics-token-vowtw
Namespace:	techlab-hospital
Annotations:    kubernetes.io/created-by=openshift.io/create-dockercfg-secrets
                kubernetes.io/service-account.name=view-metrics
[...]
Data
====
namespace:	16 bytes
service-ca.crt:	2186 bytes
token:		eyJhbGciOiJS...
```

The capture below shows this panel. The `tenant` matches the OpenShift project. It is here set to `techlab-demo-2`.

![grafana-ds](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/assets/hosa-grafana-ds.png)


### Configure a new Grafana dashboard

A sample Grafana dashboard export is provided within the `grafana-dashboard.json` file. Name of datasource and criteria for metrics should be adapted.
