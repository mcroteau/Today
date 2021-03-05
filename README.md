# Today ◆-◼-▲

### Entity Management

Today is a Free business application 
dedicated managing People, Places and Organizations.

Feel "free" to clone and run on your own to check it
out.

## Run
* install openjdk 11 https://adoptopenjdk.net/
* clone repo
* cd into Sequence
* run project    
    `./mvnw jetty:run -Dspring.profiles.active=Dev`
    
The project is completely open source. 
You just can't make money from it.

For detailed instructions...

DFW7S282

${pageContext.request.contextPath}


	<bean depends-on="dataSource" class="org.springframework.beans.factory.config.MethodInvokingBean">
		<property name="targetClass" value="org.hsqldb.util.DatabaseManagerSwing" />
		<property name="targetMethod" value="main" />
		<property name="arguments">
			<list>
				<value>--url</value>
				<value>jdbc:h2:mem:dataSource</value>
				<value>--user</value>
				<value>sa</value>
				<value>--password</value>
				<value></value>
			</list>
		</property>
	</bean>