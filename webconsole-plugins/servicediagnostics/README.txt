OSGi Service Diagnostics and WebConsole Plugin
==============================================

This projects aims at easing diagnostics of OSGi services and finding about missing dependencies.

Typically in a large system with many cascading dependencies managed by different trackers such as DeclarativeService, DependencyManager or others, tracking the root cause of a top level service not being started can become very cumbersome. When building service oriented architectures, it is often the case that a single missing requirement will lock a full stack of services, but to find that one requirement is like finding a needle in a haystack!

The basic idea here is to ask each dependency manager instance about its unresolved dependencies, merge all answers and filter the result to keep only the root causes. 

Typically, if A depends on B which depends on C which depends on D, and D is nowhere to be found, I need only show the "C -> D" missing requirement; if D is resolved, then the whole stack is unlocked.

Similarly, if D is known by another dependency management system, but unregistered because it depends on E which is missing, then only the "D -> E" requirement is relevant. 

Project organization (core):
============================
* servicediagnostics: the API package. It holds the main service interface as well as the plugin interface, to extend to other dependency management systems

* servicediagnostics.impl: the implementation package. It contains plugins implementations for org.apache.felix.scr and org.apache.felix.dependencymanager, as well as the main service implementation.

* servicediagnostics.webconsole: a Felix WebConsole plugin that displays a graphical view of the diagnostics result. Alternatively it can also show a graph of all services currently registered in the service registry and the bundles using them. See Screenshot-notavail.png and Screenshot-all.png. 

The "sample" part simply contains some test classes and a helper class to launch Felix. 

How to build the project:
=========================

> mvn install

How to run:
===========
I'm providing a simple run script just to try out the service.
> ./run.sh

It should display:
[INFO] Started jetty 6.1.x at port(s) HTTP:8080

You can then point your browser to 
http://localhost:8080/system/console/servicegraph

(login/password is admin/admin)
then click on either "Unavail" or "All". 
The "bubbles" can be dragged around for better readability. The colors are random and don't mean anything. 
On the "All" view, arrows point from bundles to the services they use. 
On the "Unavail" view, arrows point from a leaf component to its missing dependencies.

(note: the lib directory is provided for dependencies that are not readily available as OSGi bundles from maven)

Issues & TODOs:
===============
* use of JSONObject in Servlet.scala is a bit awkward, but Scala's native json is still incomplete.. 
* no support for service properties; needs to be added.
* no support for iPojo, Blueprint, basic ServiceTrackers... more plugins could be developed. I only wrote the ones i'm using.
