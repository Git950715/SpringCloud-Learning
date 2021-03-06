# SpringCloud-Parent

此项目是整个SpringCloud项目的初始项目，所有的子项目都依赖此项目中的jar。

由于每次引入jar包，导致pom中的代码过于冗余，所以使用引入父级项目的方式减少代码量，你也可以把不建立这个项目，每个项目都引入父级项目中的jar包方式构建子项目。

首先你需要构建一个maven项目，如果这个步骤不会的话可以去Google，有很多的例子。

构建完成后我们需要修改pom文件：

  1：建立的maven项目packaging起初应该是jar，你需要修改为：<packaging>pom</packaging>。
  
  2：既然是想要建立SpringCloud项目，那么首先就需要构建一个SpringBoot项目，毕竟是依赖于它的。那么这样的话就需要引入SpringBoot的jar。
    	
	<parent>
	    <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>2.0.3.RELEASE</version>
            <relativePath />
	</parent>
	
    我引用的SpringBoot是2.0.3版本。
    
  3：<modules></modules>可以将子项目的artifactId放入其中，引用到子项目，这样打包的时候就可以都打包了。
  
  4：properties都是一些简单的配置，由这可以看出我SpringCloud的版本使用的是Finchley.RELEASE。
    
    <properties>
	    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
            <java.version>1.8</java.version>
	    <spring-cloud.version>Finchley.RELEASE</spring-cloud.version>
	</properties>
	  
  5：为了保持子项目中的SpringCloud版本保持一致，使用dependencyManagement。
    
    <dependencyManagement>
	    <dependencies>
	        <dependency>
		    <groupId>org.springframework.cloud</groupId>
		    <artifactId>spring-cloud-dependencies</artifactId>
		    <version>${spring-cloud.version}</version>
		    <type>pom</type>
		    <scope>import</scope>
		</dependency>
	    </dependencies>
	</dependencyManagement>
	${spring-cloud.version}引用的是properties中的spring-cloud.version。
	  
    这样父级项目配置就完成了，下一章说明怎么引入父级项目并开始SpringCloud之旅。
    
    其余配置不再一一说明，具体源码可以查看本项目源码。
