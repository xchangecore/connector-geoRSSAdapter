connector-geoRSSAdapter
=======================

A GWT GUI that allows user to specify a URL for the RSS feed which each item of the feed will be created as an Alert or Incident Work Product on the XchangeCore.


Dependencies:
connector-util
connector-async

To Build:
1. Use maven and run "mvn clean install" to build the dependencies.
2. Run "mvn clean install" to build geoRSSAdapter.

To Run:
1. Copy the georssadapter/src/main/resources/contexts/georssadapter-context to the same directory of the SampleGeorssAdapter.jar file.
2. Use an editor to open the georssadapter-context file.
3. Look for the webServiceTemplate bean, replace the "defaultUri" to the XchangeCore you are using to run this adapter to create the incidents.
   If not localhost, change http to https, example "https://test4.xchangecore.leidos.com/uicds/core/ws/services"
4. Change the "credentials" to a valid username and password that can access your XchangeCore.
5. Open a cygwin or windows, change directory to where the SampleGeorssAdapter.jar file is located, run "java -jar SampleGeorssAdapter.jar"
