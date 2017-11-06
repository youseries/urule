/**
 * Created by Jacky.gao on 2016/8/10.
 */
import React,{Component,PropTypes} from 'react';
export default class QuickStart extends Component{
    render(){
        return (
            <div style={{fontSize: '13px',fontFamily: 'Microsoft YaHei UI, Microsoft YaHei',margin:'10px'}}><h1>URule Console Quick Start</h1>
            首先下载到URule Console及URule Core的jar包，如果是Maven项目，则需要在pom.xml中添加如下依赖：
            <pre style={{fontSize: '15px',border:'solid #666666 1px;padding: 10px'}}>
            &lt;dependency&gt;<br/>
                &lt;groupId&gt;com.bstek.urule&lt;/groupId&gt;<br/>
                &lt;artifactId&gt;urule-console&lt;/artifactId&gt;<br/>
                &lt;version&gt;[version]&lt;/version&gt;<br/>
            &lt;/dependency&gt;<br/>
            </pre>
            因为URule Console中架构在Spring之上的，所以需要加载URule Console中提供的Spring配置文件，具体方法是打开web.xml文件，在其中添加Spring的ContextLoaderListener，具体配置如下：
            <pre style={{fontSize: '15px',border:'solid #666666 1px;padding: 10px'}}>
            &lt;listener&gt;<br/>
                &lt;listener-class&gt;org.springframework.web.context.ContextLoaderListener&lt;/listener-class&gt;<br/>
            &lt;/listener&gt;<br/>
            &lt;context-param&gt;<br/>
                &lt;param-name&gt;contextConfigLocation&lt;/param-name&gt;<br/>
                &lt;param-value&gt;/WEB-INF/context.xml&lt;/param-value&gt;<br/>
            &lt;/context-param&gt;<br/>
            </pre>
            上面的context.xml是个位于当前项目WEB-INF目录下的spring配置文件，用于引用URule Console中的Spring配置文件，实现URule Console中Spring配置文件的间接加载，当前context.xml中加载URule Console的Spring配置文件方法就是添加一条import语句，如下所示：
            <pre style={{fontSize: '15px',border:'solid #666666 1px;padding: 10px'}}>
            &lt;import resource="classpath:urule-console-context.xml"/&gt;
            </pre>
            URule中有一些默认的允许外部覆盖的属性，比如用于指定当前知识库存放目录的urule.repository.dir属性、用于指定URule Console控制台首页显示页面的urule.welcomePage属性等，对于这些属性我们可以新建一个properties文件，在添加设置这些属性值，然后在我们的context.xml文件中通过如下方法加载即可：
            <pre style={{fontSize: '15px',border:'solid #666666 1px;padding: 10px'}}>
            &lt;bean parent="urule.props"&gt;<br/>
                &lt;property name="location"&gt;<br/>
                    &lt;value&gt;/WEB-INF/configure.properties&lt;/value&gt;<br/>
                &lt;/property&gt;<br/>
            &lt;/bean&gt;<br/>
            </pre>
            这里ID为urule.props的Bean是URule中提供的一个用于加载Spring外部属性文件的Bean，通过上述方式就可以将外部属性文件加载并覆盖URule中默认的属性值，当然如果我们的应用中也有属性需要加载，也可以放在这个文件中一并加载，因为这里通过urule.props加载的spring的属性文件就是标准的spring属性文件加载方式。<br/>
            最后我们还需要在项目的web.xml当中添加URule Console中的一个Servlet，这个Servlet负责控制台中所有页面与服务端的交互，配置信息如下：
            <pre style={{fontSize: '15px',border:'solid #666666 1px;padding: 10px'}}>
                &lt;servlet&gt;<br/>
                    &lt;servlet-name&gt;uruleServlet&lt;/servlet-name&gt;<br/>
                    &lt;servlet-class&gt;com.bstek.urule.console.servlet.URuleServlet&lt;/servlet-class&gt;<br/>
                &lt;/servlet&gt;<br/>
                &lt;servlet-mapping&gt;<br/>
                    &lt;servlet-name&gt;uruleServlet&lt;/servlet-name&gt;<br/>
                    &lt;url-pattern&gt;/urule/&#42;&lt;/url-pattern&gt;<br/>
                &lt;/servlet-mapping&gt;
            </pre>
            在上面的servlet配置当中，需要注意的是servlet-mapping中的url-pattern的值必须是/urule/&#42;。<br/>
            <br/>运行项目，浏览如下地址，就可以看到URule Console提供的控制台：
            <pre style={{fontSize: '15px',border:'solid #666666 1px;padding: 10px'}}>
            http://localhost:[port]/[contextPath]/urule/frame
            </pre>
            <div>
                了解更多信息，请至<a href="http://wiki.bsdn.org/display/urule2" target="_blank">http://wiki.bsdn.org/display/urule2</a>
            </div>
        </div>
        );
    }
};
