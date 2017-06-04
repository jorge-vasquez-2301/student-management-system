# Student management system

<ol>
    <li><a href="#design">Design choices</a></li>
    <li><a href="#requirements">Requirements</a></li>
    <li><a href="#instructions">Running instructions</a></li>
</ol>

**<a name="design"><h2>Design choices</h2></a>**
<ul>
    <li>An <b>MVC architecture</b> is used for the project, exposing a <b>RESTful API</b></li>
    <li>
        Spring Boot was the framework chosen for this project, this is because it greatly simplifies the process of configuring Spring itself, the following Maven starter dependencies where included:
        <ul>
            <li>spring-boot-starter-web: This allows to use Spring MVC for the REST API</li>
            <li>spring-boot-starter-data-jpa: This allows to use Spring Data automatic repositories with Hibernate as ORM</li>
            <li>spring-boot-starter-cache: This allows to enable caching support</li>
            <li>spring-boot-starter-test: This provides support for integration tests with MockMvc</li>
        </ul>
    </li>
    <li><b>Maven</b> is used as the build and dependencies management tool for this project</li>
    <li>For storing information about students, an <b>H2 in-memory database</b> is used</li>
    <li>For better performance, results for search request are cached, using a simple cache provider based on ConcurrentHashMap
    <li>Searches are case insensitive</li>
</ul>

**<a name="requirements"><h2>Requirements</h2></a>**
<ul>
    <li>JDK 8, you can get it from <a href="http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html" target="_blank">here</a></li>
    <li>Maven, you can find installation instructions <a href="https://maven.apache.org/install.html" target="_blank">here</a></li>
</ul>

**<a name="instructions"><h2>Running instructions</h2></a>**
<ul>
    <li>For running the application from the source code, just issue the command <code>mvn spring-boot:run</code> in the project's root directory, where the pom.xml file resides</li>
    <li>For running tests for the application, just issue the command <code>mvn test</code> in the project's root directory</li>
    <li>Once the application is started, it listens on port 8080 expecting for requests</li>
</ul>
