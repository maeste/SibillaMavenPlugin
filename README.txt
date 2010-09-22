Please read http://code.google.com/p/sibilla/wiki/ContributorGuidelines to properly setup the working environment.

To run the plugin use the following command (be sure to specify the right version):

  mvn it.javalinux.sibilla.plugins:maven-sibilla-plugin:1.0-SNAPSHOT:sibilla

Otherwise edit your ~/.m2/settings.xml adding:

  <pluginGroups>
    <pluginGroup>it.javalinux.sibilla.plugins</pluginGroup>
  </pluginGroups>

and simply use:

  mvn sibilla:sibilla
