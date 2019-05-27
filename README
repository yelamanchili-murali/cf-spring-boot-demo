# Demo Documentation

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

# 

## Pre-Requisites

### Development setup
* Java 1.8 or higher
* Apache Maven (this demo uses **Apache Maven 3.5.4**)
* Git install (optional - demo code is cloned fro Github)

### Step 1. Selecting a Provider : Pivotal Web Services

Sign up for a free account on a hosted Cloud Foundry Provider. 

Cloud Foundry website has a [list of certified providers](https://www.cloudfoundry.org/certified-platforms/) who meet the standard which runs all the core features of Cloud Foundry and value added services. 

For this demo, we will be using Pivotal Web Services, an instance of PCF hosted by Pivotal.

Get a free [Pivotal Web Services](https://try.run.pivotal.io/gettingstarted) account

### Step 2. Download and Install CF CLI

Download and install the CF CLI for your OS here - https://github.com/cloudfoundry/cli#downloads

Once you have installed the CLI tool, verify that it works by opening a terminal and and running 
```sh
cf --version
```
You should something similar to
```sh
cf version 6.44.0+5de0f0d02.2019-05-01
```
If you see a result like this, the CLI is installed correctly and you can proceed.

Additional resources:
* [Installing the cf CLI](https://docs.cloudfoundry.org/cf-cli/install-go-cli.html)
* [CF CLI Reference Guide](http://cli.cloudfoundry.org/en-US/cf/)

## Authenticate with Pivotal Web Services
The CLI tool talks to the Cloud Foundry API. Pivotal Web Services has to know who you are before it can allow any actions like pushing an app or listing routes, because this would be tied to your account. 

### Step 3. Login to the Provider
Run the following command to authenticate with Pivotal Web Services.
```sh
cf login
```

For Pivotal Web Services, use the following values:

`API Endpoint`: https://api.run.pivotal.io
`Email`: (Email address used in **Step 1**)
`Password`: (Password used in **Step 1**)

On successful login, you are authenticated with Pivotal Web Services.
The CLI tool stores a _token_ received on successful authentication, instead of saving your password.

Verify your target _org_, _space_ details by running the following command:
```sh
cf target
```
If successful should return something similar to the following:
```sh
api endpoint:   https://api.run.pivotal.io
api version:    2.134.0
user:           <your-email-address>
org:            power-cf-demo-org
space:          development
```

## Push an application the Pivotal Cloud Foundry (PCF)

Basic workflow:

* Ensure you are logged in with PCF
* Navigate to the directory containing the application you wish to push
* Run `cf push`
* Access the application's URL 

Overview of activities trigged by `cf push`:
* CLI gather your application's files and uploads them to PCF.
* Within PCF.
    1. The Cloud Controller finds the needed Buildpack.
    2. The Buildpack builds an application image called a *Droplet*.
    3. *Diego* takes that *Droplet* and runs it in a Container(*Diego Cell*).
* Reports success or any errors.

### Step 4. Push our "**_cf-spring-boot-demo_**" Spring Boot app

The demo is divided into 2 parts:
* Part 1 - Covers pushing an app to CloudFoundry, scaling the app and demonstrate failover and recovery
* Part 2 - Covers creating and binding a service (_rediscloud_) to the app 

The source code for this demo is available at https://github.com/yelamanchili-murali/cf-spring-boot-demo.

Clone the source code to your local host
```sh
git clone https://github.com/yelamanchili-murali/cf-spring-boot-demo
```
Checkout **Part 1** of the demo 
```sh
cd cf-spring-boot-demo
git fetch --all
git checkout tags/demo-part-1
```
The directory contains a file named **'manifest.yml'**. This file is an App Manifest file.
Although it is possible to use the **CF CLI** to configure the environment and deploy our apps, a __manifest__ file provides consistency and reproducibility and helps automate app deployment.

For the purpose of this demo we will employ the **'manifest.yml'** file to deploy our apps.
The contents of the manifest file should be as follows:
```sh
---
applications:
  - name: cf-spring-boot-demo
    buildpacks:
      - java_buildpack
    path: target/cf-spring-boot-demo-0.0.1-SNAPSHOT.jar
    memory: 650M
    routes:
    - route: hello-world-apps.cfapps.io
```

Package the app by running 
```sh
mvn package
```

Push the app to PCF by running
```sh
cf push
```

If there were no errors, the app should now start up and be in the RUNNING state at this point.

To verify that the app's status, run the following command:
```sh
cf app cf-spring-boot-demo
```

Verify that the app can service requests by sending a "GET https://hello-world-apps.cfapps.io/hello" using your favourite REST client. 
The response should at this stage be:
```json
{
    "message": "Hello World!"
}
```

### Step 5: View app logs

Run the following command in a terminal to view the logs:
```sh
cf logs cf-spring-boot-demo
```

## Scale our app, and demonstrate failover and recovery
### Step 6: Scale "**_cf-spring-boot-demo_**"
At some point you will need to scale the application up, running more instances to handle more traffic.

Note that it is possible to use the Autoscaling mechanism, but for the purpose of this demo, we will rely on the CLI to scale up.

For details on Autoscaling, refer to [Autoscaler documentation](https://docs.run.pivotal.io/appsman-services/autoscaler/using-autoscaler.html)

Let us scale up our app to **3 instances** using the following command:

```sh
cf scale cf-spring-boot-demo -i 3
```

In a few seconds, we should notice that 3 instances of the app are RUNNING. 
Executing the command `cf app cf-spring-boot-demo` should return something similar to:

```sh
$ cf app cf-spring-boot-demo
Showing health and status for app cf-spring-boot-demo in org power-cf-demo-org / space development as ************i.*****@****.com...

name:              cf-spring-boot-demo
requested state:   started
routes:            hello-world-apps.cfapps.io
last uploaded:     Mon 27 May 17:34:41 AEST 2019
stack:             cflinuxfs3
buildpacks:        java_buildpack

type:           web
instances:      3/3
memory usage:   650M
     state     since                  cpu    memory           disk           details
#0   running   2019-05-27T07:35:03Z   0.4%   172.5M of 650M   126.2M of 1G
#1   running   2019-05-27T07:44:49Z   0.6%   139.6M of 650M   126.2M of 1G
#2   running   2019-05-27T07:44:56Z   0.4%   140M of 650M     126.2M of 1G
```

At this stage, PCF will begin to balance the load across the 3 instances of the app. 

### Step 7: Configure failover and recovery for "**_cf-spring-boot-demo_**"

PCF can automatically recover from an app instance crash. To see how this works, we will terminate 2 of the app's instances, and observe that the requests will be routed to the surviving instance. 

In a few seconds, another 2 instances of the app are automatically restored thereby recovering back to the desired state.

To terminate an instance, login to the PCF console and Terminate the app instances. 
Alternatively, we can send a POST request to the shutdown the app to a custom **/shutdown** endpoint using a REST client.

To ensure that our request is targeted to a specific instance, we must include a HTTP header **X-CF-APP-INSTANCE**.
An example request is shown below:
```sh
$ curl app.example.com -H "X-CF-APP-INSTANCE":"YOUR-APP-GUID:YOUR-INSTANCE-INDEX"
```

Determine the <YOUR-APP-GUID> by running the following command:
```sh
cf app cf-spring-boot-demo --guid
```

Terminate instances #1 and #2, and observe that PCF automatically restores the instances in a few seconds while routing the requests to the surviving instance until the state if restored again.

## Create and Bind a Service

This is Part 2 of the demo. Checkout the source for Part 2 from Git 
```sh
git checkout tags/demo-part-2
```

In this section, we will create and bind a service to our application.
For the purpose of this demo, we will create and bind an instance of the **_rediscloud_** service.

Notice that the contents of **manifest.yml** will have changed.
A new **_services_** section has been included in the file:
```sh
---
applications:
  - name: cf-spring-boot-demo
    buildpacks:
      - java_buildpack
    path: target/cf-spring-boot-demo-0.0.1-SNAPSHOT.jar
    memory: 650M
    routes:
    - route: hello-world-apps.cfapps.io
    services:
    - demo-redis
```

**_demo-redis_** is the name of the service we want to create and bind our app to.

A list of services available in the Marketplace can be retrieved by running the following command:
```sh
cf marketplace
```

For details of the various plans available under the **_rediscloud_** service, run the command `cf marketplace -s rediscloud`

You should see output similar to this:
```sh
$ cf marketplace -s rediscloud
Getting service plan information for service rediscloud as *****@**.com...
OK

service plan   description          free or paid
100mb          Basic                paid
250mb          Mini                 paid
500mb          Small                paid
1gb            Standard             paid
2-5gb          Medium               paid
5gb            Large                paid
10gb           X-Large              paid
50gb           XX-Large             paid
30mb           Lifetime Free Tier   free
```

For the purpose of this demo, we will choose the free service plan **30mb**

### Step 8: Create a Service

Create a _rediscloud_ service instance named **demo-redis** by running:
```sh
cf create-service rediscloud 30mb demo-redis
```

### Step 9: Bind a service

There are two ways to bind a service
* using the CF CLI by executing the command 
```sh
cf bind-service cf-spring-boot-demo demo-redis
```
* adding a *Services* section to the _manifest.yml_ (we will use this approach for the purpose of this demo)

```yaml
---
applications:
  - name: cf-spring-boot-demo
    buildpacks: 
      - java_buildpack
    path: target/cf-spring-boot-demo-0.0.1-SNAPSHOT.jar
    memory: 650M
    routes:
    - route: hello-world-apps.cfapps.io
    services:
    - demo-redis
```

### Step 10: Push the app updates to CF

Execute the following commands:
```sh
mvn package
```
followed by
```sh
cf push
```

Resend "GET https://hello-world-apps.cfapps.io/hello" using your favourite REST client. 
The response should at this stage should be similar to:
```json
{
    "message": "Hello World!",
    "value": "1"
}
```

Note that the app's GUID would have changed, and fetch it again by running `cf app cf-spring-boot-demo --guid`, if you wish to try sending a request to shutdown the app.