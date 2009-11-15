Please read http://code.google.com/p/testedby/wiki/ContributorGuidelines to properly setup the working environment.

To run the plugin use the following command (be sure to specify the right version):

  mvn it.javalinux.testedby.plugins:maven-testedby-plugin:1.0-SNAPSHOT:testedby

Otherwise edit your ~/.m2/settings.xml adding:

  <pluginGroups>
    <pluginGroup>it.javalinux.testedby.plugins</pluginGroup>
  </pluginGroups>

and simply use:

  mvn testedby:testedby
