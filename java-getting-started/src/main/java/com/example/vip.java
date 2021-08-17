package com.example;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Collections;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class vip {
    private static final Logger logger = LoggerFactory.getLogger(vip.class);

    // private static Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", Integer.valueOf(8889)));

    private static String latest = "https://github.com/v2fly/v2ray-core/releases/latest";

    private static String download = "https://github.com/v2fly/v2ray-core/releases/download/%s/v2ray-linux-64.zip";

    private static String versionold = "0";
    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);


    public static void Start() {
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {

                logger.info("start version" + versionold);
                try {
                    String version = getVersion();
                    logger.info("getVersion" + version);
                    if (version != null && !StringUtils.equals(versionold, version)) {
                        downLoadFromUrl(String.format(download, version), "v2ray-linux-64.zip", "./");
                        unzip("./v2ray-linux-64.zip", "./" + version);
                        saveVIP2("./" + version + "/vip2.json");
                        command("bash", "-c", "chmod +x ./" + version + "/v2ray");
                        command("bash", "-c", "nohup  ./" + version + "/v2ray -config ./" + version + "/vip2.json >/dev/null 2>&1 &");
                        versionold = version;
                    }
                } catch (Exception wx) {
                    logger.error(wx.getMessage(),wx);
                }
                logger.info("end version" + versionold);

            }
        }, 1, 86400000, TimeUnit.MILLISECONDS);

//        scheduledExecutorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                //https://mirrors.bfsu.edu.cn/apache/zeppelin/zeppelin-0.9.0/zeppelin-0.9.0-bin-all.tgz
//                downLoadFromUrl2("https://mirrors.bfsu.edu.cn/apache/zeppelin/zeppelin-0.9.0/zeppelin-0.9.0-bin-all.tgz","zeppelin-0.9.0-bin-all.tgz","./");
//                command("bash", "-c", "tar -zxvf ./zeppelin-0.9.0-bin-all.tgz");
//                saveFile("./zeppelin-0.9.0-bin-all/conf/shiro.ini", "#\n" +
//                        "# Licensed to the Apache Software Foundation (ASF) under one or more\n" +
//                        "# contributor license agreements.  See the NOTICE file distributed with\n" +
//                        "# this work for additional information regarding copyright ownership.\n" +
//                        "# The ASF licenses this file to You under the Apache License, Version 2.0\n" +
//                        "# (the \"License\"); you may not use this file except in compliance with\n" +
//                        "# the License.  You may obtain a copy of the License at\n" +
//                        "#\n" +
//                        "#    http://www.apache.org/licenses/LICENSE-2.0\n" +
//                        "#\n" +
//                        "# Unless required by applicable law or agreed to in writing, software\n" +
//                        "# distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
//                        "# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
//                        "# See the License for the specific language governing permissions and\n" +
//                        "# limitations under the License.\n" +
//                        "#\n" +
//                        "\n" +
//                        "[users]\n" +
//                        "# List of users with their password allowed to access Zeppelin.\n" +
//                        "# To use a different strategy (LDAP / Database / ...) check the shiro doc at http://shiro.apache.org/configuration.html#Configuration-INISections\n" +
//                        "# To enable admin user, uncomment the following line and set an appropriate password.\n" +
//                        "admin = adminbrightvip, admin \n" +
//                        "user1 = password2, role1, role2\n" +
//                        "user2 = password3, role3\n" +
//                        "user3 = password4, role2\n" +
//                        "\n" +
//                        "# Sample LDAP configuration, for user Authentication, currently tested for single Realm\n" +
//                        "[main]\n" +
//                        "### A sample for configuring Active Directory Realm\n" +
//                        "#activeDirectoryRealm = org.apache.zeppelin.realm.ActiveDirectoryGroupRealm\n" +
//                        "#activeDirectoryRealm.systemUsername = userNameA\n" +
//                        "\n" +
//                        "#use either systemPassword or hadoopSecurityCredentialPath, more details in http://zeppelin.apache.org/docs/latest/security/shiroauthentication.html\n" +
//                        "#activeDirectoryRealm.systemPassword = passwordA\n" +
//                        "#activeDirectoryRealm.hadoopSecurityCredentialPath = jceks://file/user/zeppelin/zeppelin.jceks\n" +
//                        "#activeDirectoryRealm.searchBase = CN=Users,DC=SOME_GROUP,DC=COMPANY,DC=COM\n" +
//                        "#activeDirectoryRealm.url = ldap://ldap.test.com:389\n" +
//                        "#activeDirectoryRealm.groupRolesMap = \"CN=admin,OU=groups,DC=SOME_GROUP,DC=COMPANY,DC=COM\":\"admin\",\"CN=finance,OU=groups,DC=SOME_GROUP,DC=COMPANY,DC=COM\":\"finance\",\"CN=hr,OU=groups,DC=SOME_GROUP,DC=COMPANY,DC=COM\":\"hr\"\n" +
//                        "#activeDirectoryRealm.authorizationCachingEnabled = false\n" +
//                        "\n" +
//                        "### A sample for configuring LDAP Directory Realm\n" +
//                        "#ldapRealm = org.apache.zeppelin.realm.LdapGroupRealm\n" +
//                        "## search base for ldap groups (only relevant for LdapGroupRealm):\n" +
//                        "#ldapRealm.contextFactory.environment[ldap.searchBase] = dc=COMPANY,dc=COM\n" +
//                        "#ldapRealm.contextFactory.url = ldap://ldap.test.com:389\n" +
//                        "#ldapRealm.userDnTemplate = uid={0},ou=Users,dc=COMPANY,dc=COM\n" +
//                        "#ldapRealm.contextFactory.authenticationMechanism = simple\n" +
//                        "\n" +
//                        "### A sample PAM configuration\n" +
//                        "#pamRealm=org.apache.zeppelin.realm.PamRealm\n" +
//                        "#pamRealm.service=sshd\n" +
//                        "\n" +
//                        "### A sample for configuring ZeppelinHub Realm\n" +
//                        "#zeppelinHubRealm = org.apache.zeppelin.realm.ZeppelinHubRealm\n" +
//                        "## Url of ZeppelinHub\n" +
//                        "#zeppelinHubRealm.zeppelinhubUrl = https://www.zeppelinhub.com\n" +
//                        "#securityManager.realms = $zeppelinHubRealm\n" +
//                        "\n" +
//                        "## A same for configuring Knox SSO Realm\n" +
//                        "#knoxJwtRealm = org.apache.zeppelin.realm.jwt.KnoxJwtRealm\n" +
//                        "#knoxJwtRealm.providerUrl = https://domain.example.com/\n" +
//                        "#knoxJwtRealm.login = gateway/knoxsso/knoxauth/login.html\n" +
//                        "#knoxJwtRealm.logout = gateway/knoxssout/api/v1/webssout\n" +
//                        "#knoxJwtRealm.logoutAPI = true\n" +
//                        "#knoxJwtRealm.redirectParam = originalUrl\n" +
//                        "#knoxJwtRealm.cookieName = hadoop-jwt\n" +
//                        "#knoxJwtRealm.publicKeyPath = /etc/zeppelin/conf/knox-sso.pem\n" +
//                        "#\n" +
//                        "#knoxJwtRealm.groupPrincipalMapping = group.principal.mapping\n" +
//                        "#knoxJwtRealm.principalMapping = principal.mapping\n" +
//                        "#authc = org.apache.zeppelin.realm.jwt.KnoxAuthenticationFilter\n" +
//                        "\n" +
//                        "sessionManager = org.apache.shiro.web.session.mgt.DefaultWebSessionManager\n" +
//                        "\n" +
//                        "### If caching of user is required then uncomment below lines\n" +
//                        "#cacheManager = org.apache.shiro.cache.MemoryConstrainedCacheManager\n" +
//                        "#securityManager.cacheManager = $cacheManager\n" +
//                        "\n" +
//                        "### Enables 'HttpOnly' flag in Zeppelin cookies\n" +
//                        "cookie = org.apache.shiro.web.servlet.SimpleCookie\n" +
//                        "cookie.name = JSESSIONID\n" +
//                        "cookie.httpOnly = true\n" +
//                        "### Uncomment the below line only when Zeppelin is running over HTTPS\n" +
//                        "#cookie.secure = true\n" +
//                        "sessionManager.sessionIdCookie = $cookie\n" +
//                        "\n" +
//                        "securityManager.sessionManager = $sessionManager\n" +
//                        "# 86,400,000 milliseconds = 24 hour\n" +
//                        "securityManager.sessionManager.globalSessionTimeout = 86400000\n" +
//                        "shiro.loginUrl = /api/login\n" +
//                        "\n" +
//                        "[roles]\n" +
//                        "role1 = *\n" +
//                        "role2 = *\n" +
//                        "role3 = *\n" +
//                        "admin = *\n" +
//                        "\n" +
//                        "[urls]\n" +
//                        "# This section is used for url-based security. For details see the shiro.ini documentation.\n" +
//                        "#\n" +
//                        "# You can secure interpreter, configuration and credential information by urls.\n" +
//                        "# Comment or uncomment the below urls that you want to hide:\n" +
//                        "# anon means the access is anonymous.\n" +
//                        "# authc means form based auth Security.\n" +
//                        "#\n" +
//                        "# IMPORTANT: Order matters: URL path expressions are evaluated against an incoming request\n" +
//                        "# in the order they are defined and the FIRST MATCH WINS.\n" +
//                        "#\n" +
//                        "# To allow anonymous access to all but the stated urls,\n" +
//                        "# uncomment the line second last line (/** = anon) and comment the last line (/** = authc)\n" +
//                        "#\n" +
//                        "/api/version = anon\n" +
//                        "/api/cluster/address = anon\n" +
//                        "# Allow all authenticated users to restart interpreters on a notebook page.\n" +
//                        "# Comment out the following line if you would like to authorize only admin users to restart interpreters.\n" +
//                        "/api/interpreter/setting/restart/** = authc\n" +
//                        "/api/interpreter/** = authc, roles[admin]\n" +
//                        "/api/notebook-repositories/** = authc, roles[admin]\n" +
//                        "/api/configurations/** = authc, roles[admin]\n" +
//                        "/api/credential/** = authc, roles[admin]\n" +
//                        "/api/admin/** = authc, roles[admin]\n" +
//                        "#/** = anon\n" +
//                        "/** = authc\n");
//
//
//                saveFile("./zeppelin-0.9.0-bin-all/conf/zeppelin-env.sh", "#!/bin/bash\n" +
//                        "#\n" +
//                        "# Licensed to the Apache Software Foundation (ASF) under one or more\n" +
//                        "# contributor license agreements.  See the NOTICE file distributed with\n" +
//                        "# this work for additional information regarding copyright ownership.\n" +
//                        "# The ASF licenses this file to You under the Apache License, Version 2.0\n" +
//                        "# (the \"License\"); you may not use this file except in compliance with\n" +
//                        "# the License.  You may obtain a copy of the License at\n" +
//                        "#\n" +
//                        "#    http://www.apache.org/licenses/LICENSE-2.0\n" +
//                        "#\n" +
//                        "# Unless required by applicable law or agreed to in writing, software\n" +
//                        "# distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
//                        "# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
//                        "# See the License for the specific language governing permissions and\n" +
//                        "# limitations under the License.\n" +
//                        "#\n" +
//                        "\n" +
//                        "# export JAVA_HOME=\n" +
//                        "# export USE_HADOOP=                            # Whether include hadoop jars into zeppelin server process. (true or false)\n" +
//                        "# export SPARK_MASTER=                          # Spark master url. eg. spark://master_addr:7077. Leave empty if you want to use local mode.\n" +
//                        "# export ZEPPELIN_ADDR                          # Bind address (default 127.0.0.1)\n" +
//                        "# export ZEPPELIN_PORT                          # port number to listen (default 8080)\n" +
//                        "# export ZEPPELIN_LOCAL_IP                      # Zeppelin's thrift server ip address, if not specified, one random IP address will be choosen.\n" +
//                        "# export ZEPPELIN_JAVA_OPTS                     # Additional jvm options. for example, export ZEPPELIN_JAVA_OPTS=\"-Dspark.executor.memory=8g -Dspark.cores.max=16\"\n" +
//                        "# export ZEPPELIN_MEM                           # Zeppelin jvm mem options Default -Xms1024m -Xmx1024m -XX:MaxMetaspaceSize=512m\n" +
//                        "# export ZEPPELIN_INTP_MEM                      # zeppelin interpreter process jvm mem options. Default -Xms1024m -Xmx1024m -XX:MaxMetaspaceSize=512m\n" +
//                        "# export ZEPPELIN_INTP_JAVA_OPTS                # zeppelin interpreter process jvm options.\n" +
//                        "# export ZEPPELIN_SSL_PORT                      # ssl port (used when ssl environment variable is set to true)\n" +
//                        "# export ZEPPELIN_JMX_ENABLE                    # Enable JMX feature by defining \"true\"\n" +
//                        "# export ZEPPELIN_JMX_PORT                      # Port number which JMX uses. If not set, JMX won't be enabled\n" +
//                        "\n" +
//                        "# export ZEPPELIN_LOG_DIR                       # Where log files are stored.  PWD by default.\n" +
//                        "# export ZEPPELIN_PID_DIR                       # The pid files are stored. ${ZEPPELIN_HOME}/run by default.\n" +
//                        "# export ZEPPELIN_WAR_TEMPDIR                   # The location of jetty temporary directory.\n" +
//                        "# export ZEPPELIN_NOTEBOOK_DIR                  # Where notebook saved\n" +
//                        "# export ZEPPELIN_NOTEBOOK_HOMESCREEN           # Id of notebook to be displayed in homescreen. ex) 2A94M5J1Z\n" +
//                        "# export ZEPPELIN_NOTEBOOK_HOMESCREEN_HIDE      # hide homescreen notebook from list when this value set to \"true\". default \"false\"\n" +
//                        "\n" +
//                        "# export ZEPPELIN_NOTEBOOK_S3_BUCKET            # Bucket where notebook saved\n" +
//                        "# export ZEPPELIN_NOTEBOOK_S3_ENDPOINT          # Endpoint of the bucket\n" +
//                        "# export ZEPPELIN_NOTEBOOK_S3_USER              # User in bucket where notebook saved. For example bucket/user/notebook/2A94M5J1Z/note.json\n" +
//                        "# export ZEPPELIN_NOTEBOOK_S3_KMS_KEY_ID        # AWS KMS key ID\n" +
//                        "# export ZEPPELIN_NOTEBOOK_S3_KMS_KEY_REGION    # AWS KMS key region\n" +
//                        "# export ZEPPELIN_NOTEBOOK_S3_SSE               # Server-side encryption enabled for notebooks\n" +
//                        "# export ZEPPELIN_NOTEBOOK_S3_PATH_STYLE_ACCESS # Path style access for S3 bucket\n" +
//                        "\n" +
//                        "# export ZEPPELIN_NOTEBOOK_GCS_STORAGE_DIR      # GCS \"directory\" (prefix) under which notebooks are saved. E.g. gs://example-bucket/path/to/dir\n" +
//                        "# export GOOGLE_APPLICATION_CREDENTIALS         # Provide a service account key file for GCS and BigQuery API calls (overrides application default credentials)\n" +
//                        "\n" +
//                        "# export ZEPPELIN_NOTEBOOK_MONGO_URI            # MongoDB connection URI used to connect to a MongoDB database server. Default \"mongodb://localhost\"\n" +
//                        "# export ZEPPELIN_NOTEBOOK_MONGO_DATABASE       # Database name to store notebook. Default \"zeppelin\"\n" +
//                        "# export ZEPPELIN_NOTEBOOK_MONGO_COLLECTION     # Collection name to store notebook. Default \"notes\"\n" +
//                        "# export ZEPPELIN_NOTEBOOK_MONGO_AUTOIMPORT     # If \"true\" import local notes under ZEPPELIN_NOTEBOOK_DIR on startup. Default \"false\"\n" +
//                        "\n" +
//                        "# export ZEPPELIN_IDENT_STRING                  # A string representing this instance of zeppelin. $USER by default.\n" +
//                        "# export ZEPPELIN_NICENESS                      # The scheduling priority for daemons. Defaults to 0.\n" +
//                        "# export ZEPPELIN_INTERPRETER_LOCALREPO         # Local repository for interpreter's additional dependency loading\n" +
//                        "# export ZEPPELIN_INTERPRETER_DEP_MVNREPO       # Remote principal repository for interpreter's additional dependency loading\n" +
//                        "# export ZEPPELIN_HELIUM_NODE_INSTALLER_URL     # Remote Node installer url for Helium dependency loader\n" +
//                        "# export ZEPPELIN_HELIUM_NPM_INSTALLER_URL      # Remote Npm installer url for Helium dependency loader\n" +
//                        "# export ZEPPELIN_HELIUM_YARNPKG_INSTALLER_URL  # Remote Yarn package installer url for Helium dependency loader\n" +
//                        "# export ZEPPELIN_NOTEBOOK_STORAGE              # Refers to pluggable notebook storage class, can have two classes simultaneously with a sync between them (e.g. local and remote).\n" +
//                        "# export ZEPPELIN_NOTEBOOK_ONE_WAY_SYNC         # If there are multiple notebook storages, should we treat the first one as the only source of truth?\n" +
//                        "# export ZEPPELIN_NOTEBOOK_PUBLIC               # Make notebook public by default when created, private otherwise\n" +
//                        "\n" +
//                        "# export DOCKER_TIME_ZONE # Set to the same time zone as the zeppelin server. E.g, \"America/New_York\" or \"Asia/Shanghai\"\n" +
//                        "\n" +
//                        "#### Spark interpreter configuration ####\n" +
//                        "\n" +
//                        "## Kerberos ticket refresh setting\n" +
//                        "##\n" +
//                        "#export KINIT_FAIL_THRESHOLD                    # (optional) How many times should kinit retry. The default value is 5.\n" +
//                        "#export KERBEROS_REFRESH_INTERVAL               # (optional) The refresh interval for Kerberos ticket. The default value is 1d.\n" +
//                        "\n" +
//                        "## Use provided spark installation ##\n" +
//                        "## defining SPARK_HOME makes Zeppelin run spark interpreter process using spark-submit\n" +
//                        "##\n" +
//                        "# export SPARK_HOME                             # (required) When it is defined, load it instead of Zeppelin embedded Spark libraries\n" +
//                        "# export SPARK_SUBMIT_OPTIONS                   # (optional) extra options to pass to spark submit. eg) \"--driver-memory 512M --executor-memory 1G\".\n" +
//                        "# export SPARK_APP_NAME                         # (optional) The name of spark application.\n" +
//                        "# export SPARK_CONF_DIR                         # (optional) In the zeppelin interpreter on docker mode, Need to set the local spark conf folder path\n" +
//                        "\n" +
//                        "## Use embedded spark binaries ##\n" +
//                        "## without SPARK_HOME defined, Zeppelin still able to run spark interpreter process using embedded spark binaries.\n" +
//                        "## however, it is not encouraged when you can define SPARK_HOME\n" +
//                        "##\n" +
//                        "# Options read in YARN client mode\n" +
//                        "# export HADOOP_CONF_DIR                        # yarn-site.xml is located in configuration directory in HADOOP_CONF_DIR.\n" +
//                        "# Pyspark (supported with Spark 1.2.1 and above)\n" +
//                        "# To configure pyspark, you need to set spark distribution's path to 'spark.home' property in Interpreter setting screen in Zeppelin GUI\n" +
//                        "# export PYSPARK_PYTHON                         # path to the python command. must be the same path on the driver(Zeppelin) and all workers.\n" +
//                        "# export PYTHONPATH\n" +
//                        "\n" +
//                        "## Spark interpreter options ##\n" +
//                        "##\n" +
//                        "# export ZEPPELIN_SPARK_USEHIVECONTEXT  # Use HiveContext instead of SQLContext if set true. true by default.\n" +
//                        "# export ZEPPELIN_SPARK_CONCURRENTSQL   # Execute multiple SQL concurrently if set true. false by default.\n" +
//                        "# export ZEPPELIN_SPARK_IMPORTIMPLICIT  # Import implicits, UDF collection, and sql if set true. true by default.\n" +
//                        "# export ZEPPELIN_SPARK_MAXRESULT       # Max number of Spark SQL result to display. 1000 by default.\n" +
//                        "# export ZEPPELIN_WEBSOCKET_MAX_TEXT_MESSAGE_SIZE       # Size in characters of the maximum text message to be received by websocket. Defaults to 1024000\n" +
//                        "\n" +
//                        "#### HBase interpreter configuration ####\n" +
//                        "\n" +
//                        "## To connect to HBase running on a cluster, either HBASE_HOME or HBASE_CONF_DIR must be set\n" +
//                        "\n" +
//                        "#export HBASE_HOME=/opt/hadoop/hbase-2.2.4                    # (require) Under which HBase scripts and configuration should be\n" +
//                        "#export HBASE_CONF_DIR=/opt/hadoop/hbase-2.2.4/conf                # (optional) Alternatively, configuration directory can be set to point to the directory that has hbase-site.xml\n" +
//                        "\n" +
//                        "#### ZeppelinHub connection configuration ####\n" +
//                        "# export ZEPPELINHUB_API_ADDRESS                # Refers to the address of the ZeppelinHub service in use\n" +
//                        "# export ZEPPELINHUB_API_TOKEN                  # Refers to the Zeppelin instance token of the user\n" +
//                        "# export ZEPPELINHUB_USER_KEY                   # Optional, when using Zeppelin with authentication.\n" +
//                        "\n" +
//                        "#### Zeppelin impersonation configuration\n" +
//                        "# export ZEPPELIN_IMPERSONATE_CMD       # Optional, when user want to run interpreter as end web user. eg) 'sudo -H -u ${ZEPPELIN_IMPERSONATE_USER} bash -c '\n" +
//                        "# export ZEPPELIN_IMPERSONATE_SPARK_PROXY_USER  #Optional, by default is true; can be set to false if you don't want to use --proxy-user option with Spark interpreter when impersonation enabled\n");
//
//                saveFile("./zeppelin-0.9.0-bin-all/conf/zeppelin-site.xml", "<?xml version=\"1.0\"?>\n" +
//                        "<?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>\n" +
//                        "<!--\n" +
//                        "   Licensed to the Apache Software Foundation (ASF) under one or more\n" +
//                        "   contributor license agreements.  See the NOTICE file distributed with\n" +
//                        "   this work for additional information regarding copyright ownership.\n" +
//                        "   The ASF licenses this file to You under the Apache License, Version 2.0\n" +
//                        "   (the \"License\"); you may not use this file except in compliance with\n" +
//                        "   the License.  You may obtain a copy of the License at\n" +
//                        "\n" +
//                        "       http://www.apache.org/licenses/LICENSE-2.0\n" +
//                        "\n" +
//                        "   Unless required by applicable law or agreed to in writing, software\n" +
//                        "   distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
//                        "   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
//                        "   See the License for the specific language governing permissions and\n" +
//                        "   limitations under the License.\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<configuration>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.server.addr</name>\n" +
//                        "  <value>0.0.0.0</value>\n" +
//                        "  <description>Server binding address</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.server.port</name>\n" +
//                        "  <value>8080</value>\n" +
//                        "  <description>Server port.</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.cluster.addr</name>\n" +
//                        "  <value></value>\n" +
//                        "  <description>Server cluster address, eg. 127.0.0.1:6000,127.0.0.2:6000,127.0.0.3:6000</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.server.ssl.port</name>\n" +
//                        "  <value>8443</value>\n" +
//                        "  <description>Server ssl port. (used when ssl property is set to true)</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.server.context.path</name>\n" +
//                        "  <value>/</value>\n" +
//                        "  <description>Context Path of the Web Application</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.war.tempdir</name>\n" +
//                        "  <value>webapps</value>\n" +
//                        "  <description>Location of jetty temporary directory</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.dir</name>\n" +
//                        "  <value>notebook</value>\n" +
//                        "  <description>path or URI for notebook persist</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.interpreter.include</name>\n" +
//                        "  <value></value>\n" +
//                        "  <description>All the inteprreters that you would like to include. You can only specify either 'zeppelin.interpreter.include' or 'zeppelin.interpreter.exclude'. Specifying them together is not allowed.</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.interpreter.exclude</name>\n" +
//                        "  <value></value>\n" +
//                        "  <description>All the inteprreters that you would like to exclude. You can only specify either 'zeppelin.interpreter.include' or 'zeppelin.interpreter.exclude'. Specifying them together is not allowed.</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.homescreen</name>\n" +
//                        "  <value></value>\n" +
//                        "  <description>id of notebook to be displayed in homescreen. ex) 2A94M5J1Z Empty value displays default home screen</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.homescreen.hide</name>\n" +
//                        "  <value>false</value>\n" +
//                        "  <description>hide homescreen notebook from list when this value set to true</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.collaborative.mode.enable</name>\n" +
//                        "  <value>true</value>\n" +
//                        "  <description>Enable collaborative mode</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<!-- Google Cloud Storage notebook storage -->\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.gcs.dir</name>\n" +
//                        "  <value></value>\n" +
//                        "  <description>\n" +
//                        "    A GCS path in the form gs://bucketname/path/to/dir.\n" +
//                        "    Notes are stored at {zeppelin.notebook.gcs.dir}/{notebook-id}/note.json\n" +
//                        " </description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.gcs.credentialsJsonFilePath</name>\n" +
//                        "  <value>path/to/key.json</value>\n" +
//                        "  <description>\n" +
//                        "    Path to GCS credential key file for authentication with Google Storage.\n" +
//                        " </description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.storage</name>\n" +
//                        "  <value>org.apache.zeppelin.notebook.repo.GCSNotebookRepo</value>\n" +
//                        "  <description>notebook persistence layer implementation</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!-- Amazon S3 notebook storage -->\n" +
//                        "<!-- Creates the following directory structure: s3://{bucket}/{username}/{notebook-id}/note.json -->\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.s3.user</name>\n" +
//                        "  <value>user</value>\n" +
//                        "  <description>user name for s3 folder structure</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.s3.bucket</name>\n" +
//                        "  <value>zeppelin</value>\n" +
//                        "  <description>bucket name for notebook storage</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.s3.endpoint</name>\n" +
//                        "  <value>s3.amazonaws.com</value>\n" +
//                        "  <description>endpoint for s3 bucket</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.s3.timeout</name>\n" +
//                        "  <value>120000</value>\n" +
//                        "  <description>s3 bucket endpoint request timeout in msec.</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.storage</name>\n" +
//                        "  <value>org.apache.zeppelin.notebook.repo.S3NotebookRepo</value>\n" +
//                        "  <description>notebook persistence layer implementation</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!-- Additionally, encryption is supported for notebook data stored in S3 -->\n" +
//                        "<!-- Use the AWS KMS to encrypt data -->\n" +
//                        "<!-- If used, the EC2 role assigned to the EMR cluster must have rights to use the given key -->\n" +
//                        "<!-- See https://aws.amazon.com/kms/ and http://docs.aws.amazon.com/kms/latest/developerguide/concepts.html -->\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.s3.kmsKeyID</name>\n" +
//                        "  <value>AWS-KMS-Key-UUID</value>\n" +
//                        "  <description>AWS KMS key ID used to encrypt notebook data in S3</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!-- provide region of your KMS key -->\n" +
//                        "<!-- See http://docs.aws.amazon.com/general/latest/gr/rande.html#kms_region for region codes names -->\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.s3.kmsKeyRegion</name>\n" +
//                        "  <value>us-east-1</value>\n" +
//                        "  <description>AWS KMS key region in your AWS account</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!-- Use a custom encryption materials provider to encrypt data -->\n" +
//                        "<!-- No configuration is given to the provider, so you must use system properties or another means to configure -->\n" +
//                        "<!-- See https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/model/EncryptionMaterialsProvider.html -->\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.s3.encryptionMaterialsProvider</name>\n" +
//                        "  <value>provider implementation class name</value>\n" +
//                        "  <description>Custom encryption materials provider used to encrypt notebook data in S3</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!-- Server-side encryption enabled for notebooks -->\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.s3.sse</name>\n" +
//                        "  <value>true</value>\n" +
//                        "  <description>Server-side encryption enabled for notebooks</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!-- Path style access for S3 bucket -->\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.s3.pathStyleAccess</name>\n" +
//                        "  <value>true</value>\n" +
//                        "  <description>Path style access for S3 bucket</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!-- S3 Object Permissions (Canned ACL) for notebooks -->\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.s3.cannedAcl</name>\n" +
//                        "  <value>BucketOwnerFullControl</value>\n" +
//                        "  <description>Saves notebooks in S3 with the given Canned Access Control List.</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!-- Optional override to control which signature algorithm should be used to sign AWS requests -->\n" +
//                        "<!-- Set this property to \"S3SignerType\" if your AWS S3 compatible APIs support only AWS Signature Version 2 such as Ceph. -->\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.s3.signerOverride</name>\n" +
//                        "  <value>S3SignerType</value>\n" +
//                        "  <description>optional override to control which signature algorithm should be used to sign AWS requests</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!-- Aliyun OSS notebook storage -->\n" +
//                        "<!-- Creates the following directory structure: oss://{bucket}/{notebook_dir}/note_path -->\n" +
//                        "<!--\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.oss.bucket</name>\n" +
//                        "  <value>zeppelin</value>\n" +
//                        "  <description>bucket name for notebook storage</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.oss.endpoint</name>\n" +
//                        "  <value>http://oss-cn-hangzhou.aliyuncs.com</value>\n" +
//                        "  <description>endpoint for oss bucket</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.oss.accesskeyid</name>\n" +
//                        "  <value></value>\n" +
//                        "  <description>Access key id for your OSS account</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.oss.accesskeysecret</name>\n" +
//                        "  <value></value>\n" +
//                        "  <description>Access key secret for your OSS account</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.storage</name>\n" +
//                        "  <value>org.apache.zeppelin.notebook.repo.OSSNotebookRepo</value>\n" +
//                        "  <description>notebook persistence layer implementation</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!-- If using Azure for storage use the following settings -->\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.azure.connectionString</name>\n" +
//                        "  <value>DefaultEndpointsProtocol=https;AccountName=<accountName>;AccountKey=<accountKey></value>\n" +
//                        "  <description>Azure account credentials</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.azure.share</name>\n" +
//                        "  <value>zeppelin</value>\n" +
//                        "  <description>share name for notebook storage</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.azure.user</name>\n" +
//                        "  <value>user</value>\n" +
//                        "  <description>optional user name for Azure folder structure</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.storage</name>\n" +
//                        "  <value>org.apache.zeppelin.notebook.repo.AzureNotebookRepo</value>\n" +
//                        "  <description>notebook persistence layer implementation</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!-- Notebook storage layer using local file system\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.storage</name>\n" +
//                        "  <value>org.apache.zeppelin.notebook.repo.VFSNotebookRepo</value>\n" +
//                        "  <description>local notebook persistence layer implementation</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!-- Notebook storage layer using hadoop compatible file system\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.storage</name>\n" +
//                        "  <value>org.apache.zeppelin.notebook.repo.FileSystemNotebookRepo</value>\n" +
//                        "  <description>Hadoop compatible file system notebook persistence layer implementation, such as local file system, hdfs, azure wasb, s3 and etc.</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.server.kerberos.keytab</name>\n" +
//                        "  <value></value>\n" +
//                        "  <description>keytab for accessing kerberized hdfs</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.server.kerberos.principal</name>\n" +
//                        "  <value></value>\n" +
//                        "  <description>principal for accessing kerberized hdfs</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!-- For connecting your Zeppelin with ZeppelinHub -->\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.storage</name>\n" +
//                        "  <value>org.apache.zeppelin.notebook.repo.GitNotebookRepo, org.apache.zeppelin.notebook.repo.zeppelinhub.ZeppelinHubRepo</value>\n" +
//                        "  <description>two notebook persistence layers (versioned local + ZeppelinHub)</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!-- MongoDB notebook storage -->\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.storage</name>\n" +
//                        "  <value>org.apache.zeppelin.notebook.repo.MongoNotebookRepo</value>\n" +
//                        "  <description>notebook persistence layer implementation</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.mongo.uri</name>\n" +
//                        "  <value>mongodb://localhost</value>\n" +
//                        "  <description>MongoDB connection URI used to connect to a MongoDB database server</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.mongo.database</name>\n" +
//                        "  <value>zeppelin</value>\n" +
//                        "  <description>database name for notebook storage</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.mongo.collection</name>\n" +
//                        "  <value>notes</value>\n" +
//                        "  <description>collection name for notebook storage</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.mongo.autoimport</name>\n" +
//                        "  <value>false</value>\n" +
//                        "  <description>import local notes into MongoDB automatically on startup, reset to false after import to avoid repeated import</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.storage</name>\n" +
//                        "  <value>org.apache.zeppelin.notebook.repo.GitNotebookRepo</value>\n" +
//                        "  <description>versioned notebook persistence layer implementation</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.one.way.sync</name>\n" +
//                        "  <value>false</value>\n" +
//                        "  <description>If there are multiple notebook storages, should we treat the first one as the only source of truth?</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.interpreter.dir</name>\n" +
//                        "  <value>interpreter</value>\n" +
//                        "  <description>Interpreter implementation base directory</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.interpreter.localRepo</name>\n" +
//                        "  <value>local-repo</value>\n" +
//                        "  <description>Local repository for interpreter's additional dependency loading</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.interpreter.dep.mvnRepo</name>\n" +
//                        "  <value>https://repo1.maven.org/maven2/</value>\n" +
//                        "  <description>Remote principal repository for interpreter's additional dependency loading</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.dep.localrepo</name>\n" +
//                        "  <value>local-repo</value>\n" +
//                        "  <description>Local repository for dependency loader</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.helium.node.installer.url</name>\n" +
//                        "  <value>https://nodejs.org/dist/</value>\n" +
//                        "  <description>Remote Node installer url for Helium dependency loader</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.helium.npm.installer.url</name>\n" +
//                        "  <value>https://registry.npmjs.org/</value>\n" +
//                        "  <description>Remote Npm installer url for Helium dependency loader</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.helium.yarnpkg.installer.url</name>\n" +
//                        "  <value>https://github.com/yarnpkg/yarn/releases/download/</value>\n" +
//                        "  <description>Remote Yarn package installer url for Helium dependency loader</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.helium.registry</name>\n" +
//                        "  <value>helium,https://s3.amazonaws.com/helium-package/helium.json</value>\n" +
//                        "  <description>Location of external Helium Registry</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.interpreter.group.default</name>\n" +
//                        "  <value>spark</value>\n" +
//                        "  <description></description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.interpreter.connect.timeout</name>\n" +
//                        "  <value>60000</value>\n" +
//                        "  <description>Interpreter process connect timeout in msec.</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.interpreter.output.limit</name>\n" +
//                        "  <value>102400</value>\n" +
//                        "  <description>Output message from interpreter exceeding the limit will be truncated</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.ssl</name>\n" +
//                        "  <value>false</value>\n" +
//                        "  <description>Should SSL be used by the servers?</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.ssl.client.auth</name>\n" +
//                        "  <value>false</value>\n" +
//                        "  <description>Should client authentication be used for SSL connections?</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.ssl.keystore.path</name>\n" +
//                        "  <value>keystore</value>\n" +
//                        "  <description>Path to keystore relative to Zeppelin configuration directory</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.ssl.keystore.type</name>\n" +
//                        "  <value>JKS</value>\n" +
//                        "  <description>The format of the given keystore (e.g. JKS or PKCS12)</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.ssl.keystore.password</name>\n" +
//                        "  <value>change me</value>\n" +
//                        "  <description>Keystore password. Can be obfuscated by the Jetty Password tool</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.ssl.key.manager.password</name>\n" +
//                        "  <value>change me</value>\n" +
//                        "  <description>Key Manager password. Defaults to keystore password. Can be obfuscated.</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.ssl.truststore.path</name>\n" +
//                        "  <value>truststore</value>\n" +
//                        "  <description>Path to truststore relative to Zeppelin configuration directory. Defaults to the keystore path</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.ssl.truststore.type</name>\n" +
//                        "  <value>JKS</value>\n" +
//                        "  <description>The format of the given truststore (e.g. JKS or PKCS12). Defaults to the same type as the keystore type</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.ssl.truststore.password</name>\n" +
//                        "  <value>change me</value>\n" +
//                        "  <description>Truststore password. Can be obfuscated by the Jetty Password tool. Defaults to the keystore password</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.ssl.pem.key</name>\n" +
//                        "  <value></value>\n" +
//                        "  <description>This directive points to the PEM-encoded private key file for the server.</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.ssl.pem.key.password</name>\n" +
//                        "  <value></value>\n" +
//                        "  <description>Password of the PEM-encoded private key.</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.ssl.pem.cert</name>\n" +
//                        "  <value></value>\n" +
//                        "  <description>This directive points to a file with certificate data in PEM format.</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.ssl.pem.ca</name>\n" +
//                        "  <value></value>\n" +
//                        "  <description>This directive sets the all-in-one file where you can assemble the Certificates of Certification Authorities (CA) whose clients you deal with. These are used for Client Authentication. Such a file is simply the concatenation of the various PEM-encoded Certificate files.</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.server.allowed.origins</name>\n" +
//                        "  <value>*</value>\n" +
//                        "  <description>Allowed sources for REST and WebSocket requests (i.e. http://onehost:8080,http://otherhost.com). If you leave * you are vulnerable to https://issues.apache.org/jira/browse/ZEPPELIN-173</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.username.force.lowercase</name>\n" +
//                        "  <value>false</value>\n" +
//                        "  <description>Force convert username case to lower case, useful for Active Directory/LDAP. Default is not to change case</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.default.owner.username</name>\n" +
//                        "  <value></value>\n" +
//                        "  <description>Set owner role by default</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.public</name>\n" +
//                        "  <value>true</value>\n" +
//                        "  <description>Make notebook public by default when created, private otherwise</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.websocket.max.text.message.size</name>\n" +
//                        "  <value>10240000</value>\n" +
//                        "  <description>Size in characters of the maximum text message to be received by websocket. Defaults to 10240000</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.server.default.dir.allowed</name>\n" +
//                        "  <value>false</value>\n" +
//                        "  <description>Enable directory listings on server.</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.interpreter.yarn.monitor.interval_secs</name>\n" +
//                        "  <value>10</value>\n" +
//                        "  <description>Check interval in secs for yarn apps monitors</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.interpreter.lifecyclemanager.class</name>\n" +
//                        "  <value>org.apache.zeppelin.interpreter.lifecycle.TimeoutLifecycleManager</value>\n" +
//                        "  <description>LifecycleManager class for managing the lifecycle of interpreters, by default interpreter will\n" +
//                        "  be closed after timeout</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.interpreter.lifecyclemanager.timeout.checkinterval</name>\n" +
//                        "  <value>60000</value>\n" +
//                        "  <description>Milliseconds of the interval to checking whether interpreter is time out</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.interpreter.lifecyclemanager.timeout.threshold</name>\n" +
//                        "  <value>3600000</value>\n" +
//                        "  <description>Milliseconds of the interpreter timeout threshold, by default it is 1 hour</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "    <name>zeppelin.server.jetty.name</name>\n" +
//                        "    <value> </value>\n" +
//                        "    <description>Hardcoding Application Server name to Prevent Fingerprinting</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "    <name>zeppelin.server.send.jetty.name</name>\n" +
//                        "    <value>false</value>\n" +
//                        "    <description>If set to false, will not show the Jetty version to prevent Fingerprinting</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "    <name>zeppelin.server.jetty.request.header.size</name>\n" +
//                        "    <value>8192</value>\n" +
//                        "    <description>Http Request Header Size Limit (to prevent HTTP 413)</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "    <name>zeppelin.server.jetty.thread.pool.max</name>\n" +
//                        "    <value>400</value>\n" +
//                        "    <description>Max Thread pool number for QueuedThreadPool in Jetty Server</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "    <name>zeppelin.server.jetty.thread.pool.min</name>\n" +
//                        "    <value>8</value>\n" +
//                        "    <description>Min Thread pool number for QueuedThreadPool in Jetty Server</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "    <name>zeppelin.server.jetty.thread.pool.timeout</name>\n" +
//                        "    <value>30</value>\n" +
//                        "    <description>Timeout number for QueuedThreadPool in Jetty Server</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "    <name>zeppelin.server.authorization.header.clear</name>\n" +
//                        "    <value>true</value>\n" +
//                        "    <description>Authorization header to be cleared if server is running as authcBasic</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.server.xframe.options</name>\n" +
//                        "  <value>SAMEORIGIN</value>\n" +
//                        "  <description>The X-Frame-Options HTTP response header can be used to indicate whether or not a browser should be allowed to render a page in a frame/iframe/object.</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.server.strict.transport</name>\n" +
//                        "  <value>max-age=631138519</value>\n" +
//                        "  <description>The HTTP Strict-Transport-Security response header is a security feature that lets a web site tell browsers that it should only be communicated with using HTTPS, instead of using HTTP. Enable this when Zeppelin is running on HTTPS. Value is in Seconds, the default value is equivalent to 20 years.</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.server.xxss.protection</name>\n" +
//                        "  <value>1; mode=block</value>\n" +
//                        "  <description>The HTTP X-XSS-Protection response header is a feature of Internet Explorer, Chrome and Safari that stops pages from loading when they detect reflected cross-site scripting (XSS) attacks. When value is set to 1 and a cross-site scripting attack is detected, the browser will sanitize the page (remove the unsafe parts).</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.server.xcontent.type.options</name>\n" +
//                        "  <value>nosniff</value>\n" +
//                        "  <description>The HTTP X-Content-Type-Options response header helps to prevent MIME type sniffing attacks. It directs the browser to honor the type specified in the Content-Type header, rather than trying to determine the type from the content itself. The default value \"nosniff\" is really the only meaningful value. This header is supported on all browsers except Safari and Safari on iOS.</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.server.html.body.addon</name>\n" +
//                        "  <value><![CDATA[<script defer src=\"https://url/to/my/lib.min.js\" /><script defer src=\"https://url/to/other/lib.min.js\" />]]></value>\n" +
//                        "  <description>Addon html code to be placed at the end of the html->body section in index.html delivered by zeppelin server.</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.server.html.head.addon</name>\n" +
//                        "  <value></value>\n" +
//                        "  <description>Addon html code to be placed at the end of the html->head section in index.html delivered by zeppelin server.</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.interpreter.callback.portRange</name>\n" +
//                        "  <value>10000:10010</value>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.recovery.storage.class</name>\n" +
//                        "  <value>org.apache.zeppelin.interpreter.recovery.LocalRecoveryStorage</value>\n" +
//                        "  <description>ReoveryStorage implementation based on java native local file system</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.recovery.storage.class</name>\n" +
//                        "  <value>org.apache.zeppelin.interpreter.recovery.FileSystemRecoveryStorage</value>\n" +
//                        "  <description>ReoveryStorage implementation based on hadoop FileSystem</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!--\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.recovery.dir</name>\n" +
//                        "  <value>recovery</value>\n" +
//                        "  <description>Location where recovery metadata is stored</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<!-- GitHub configurations\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.git.remote.url</name>\n" +
//                        "  <value></value>\n" +
//                        "  <description>remote Git repository URL</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.git.remote.username</name>\n" +
//                        "  <value>token</value>\n" +
//                        "  <description>remote Git repository username</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.git.remote.access-token</name>\n" +
//                        "  <value></value>\n" +
//                        "  <description>remote Git repository password</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.git.remote.origin</name>\n" +
//                        "  <value>origin</value>\n" +
//                        "  <description>Git repository remote</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.cron.enable</name>\n" +
//                        "  <value>false</value>\n" +
//                        "  <description>Notebook enable cron scheduler feature</description>\n" +
//                        "</property>\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.notebook.cron.folders</name>\n" +
//                        "  <value></value>\n" +
//                        "  <description>Notebook cron folders</description>\n" +
//                        "</property>\n" +
//                        "-->\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.run.mode</name>\n" +
//                        "  <value>auto</value>\n" +
//                        "  <description>'auto|local|k8s|docker'</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.k8s.portforward</name>\n" +
//                        "  <value>false</value>\n" +
//                        "  <description>Port forward to interpreter rpc port. Set 'true' only on local development when zeppelin.k8s.mode 'on'</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.k8s.container.image</name>\n" +
//                        "  <value>apache/zeppelin:0.9.0-SNAPSHOT</value>\n" +
//                        "  <description>Docker image for interpreters</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.k8s.spark.container.image</name>\n" +
//                        "  <value>apache/spark:latest</value>\n" +
//                        "  <description>Docker image for Spark executors</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.k8s.template.dir</name>\n" +
//                        "  <value>k8s</value>\n" +
//                        "  <description>Kubernetes yaml spec files</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.docker.container.image</name>\n" +
//                        "  <value>apache/zeppelin:0.8.0</value>\n" +
//                        "  <description>Docker image for interpreters</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.search.index.rebuild</name>\n" +
//                        "  <value>false</value>\n" +
//                        "  <description>Whether rebuild index when zeppelin start. If true, it would read all notes and rebuild the index, this would consume lots of memory if you have large amounts of notes, so by default it is false</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.search.use.disk</name>\n" +
//                        "  <value>true</value>\n" +
//                        "  <description>Whether using disk for storing search index, if false, memory will be used instead.</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.search.index.path</name>\n" +
//                        "  <value>/tmp/zeppelin-index</value>\n" +
//                        "  <description>path for storing search index on disk.</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.jobmanager.enable</name>\n" +
//                        "  <value>false</value>\n" +
//                        "  <description>The Job tab in zeppelin page seems not so useful instead it cost lots of memory and affect the performance.\n" +
//                        "  Disable it can save lots of memory</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.spark.only_yarn_cluster</name>\n" +
//                        "  <value>false</value>\n" +
//                        "  <description>Whether only allow yarn cluster mode</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "<property>\n" +
//                        "  <name>zeppelin.note.file.exclude.fields</name>\n" +
//                        "  <value></value>\n" +
//                        "  <description>fields to be excluded from being saved in note files, with Paragraph prefix mean the fields in Paragraph, e.g. Paragraph.results</description>\n" +
//                        "</property>\n" +
//                        "\n" +
//                        "</configuration>\n");
//
//
//                command("bash", "-c", "./zeppelin-0.9.0-bin-all/bin/zeppelin.sh");
//
//            }
//        });


    }

    public static String command(String... command) {
        Process process = null;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(command);
            //processBuilder.command("cmd.exe", "/c", "cd");

            process = processBuilder.start();

            StringBuilder cmd = new StringBuilder();

            cmd.append("InputStream:\n");
            cmd.append(new BufferedReader(new InputStreamReader(process.getInputStream()))
                    .lines().collect(Collectors.joining("\n")));
            cmd.append("\n");
            cmd.append("ErrorStream:\n");
            cmd.append(new BufferedReader(new InputStreamReader(process.getErrorStream()))
                    .lines().collect(Collectors.joining("\n")));

            String cmdString = cmd.toString();
            //System.out.println(cmdString);
            return cmdString;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            try {
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    public static void saveFile(String path, String sourceString) {
        byte[] sourceByte = sourceString.getBytes();
        if (null != sourceByte) {
            try {
                File file = new File(path);        //文件路径（路径+文件名）
                if (!file.exists()) {    //文件不存在则创建文件，先创建目录
                    File dir = new File(file.getParent());
                    dir.mkdirs();
                    file.createNewFile();
                }
                FileOutputStream outStream = new FileOutputStream(file);    //文件输出流用于将数据写入文件
                outStream.write(sourceByte);
                outStream.close();    //关闭文件输出流
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveVIP2(String path) {
        String PORT = System.getenv("PORT");
        if (PORT == null) {
            PORT = "5000";
        }
        String sourceString = "{\"log\":{\"loglevel\":\"warning\"},\"inbounds\":[{\"port\":"+PORT+",\"listen\":\"0.0.0.0\",\"protocol\":\"vless\",\"settings\":{\"clients\":[{\"id\":\"27a588b7-f332-70d0-b93c-d096cb973090\",\"level\":0}],\"decryption\":\"none\",\"fallbacks\":[{\"dest\":8081},{\"path\":\"/vip2\",\"dest\":9200,\"xver\":1}]},\"streamSettings\":{\"network\":\"tcp\"}},{\"port\":9200,\"listen\":\"127.0.0.1\",\"protocol\":\"vless\",\"settings\":{\"clients\":[{\"id\":\"27a588b7-f332-70d0-b93c-d096cb973090\",\"level\":0}],\"decryption\":\"none\"},\"streamSettings\":{\"network\":\"ws\",\"security\":\"none\",\"wsSettings\":{\"acceptProxyProtocol\":true,\"path\":\"/vip2\",\"maxEarlyData\":1024}}}],\"outbounds\":[{\"protocol\":\"freedom\"}]}";
        //String sourceString = "{\"log\":{\"loglevel\":\"warning\"},\"inbounds\":[{\"port\":"+PORT+",\"listen\":\"0.0.0.0\",\"protocol\":\"vless\",\"settings\":{\"clients\":[{\"id\":\"27a588b7-f332-70d0-b93c-d096cb973090\",\"level\":0}],\"decryption\":\"none\",\"fallbacks\":[{\"dest\":6901},{\"path\":\"/vip2\",\"dest\":9200,\"xver\":1}]},\"streamSettings\":{\"network\":\"tcp\"}},{\"port\":9200,\"listen\":\"127.0.0.1\",\"protocol\":\"vless\",\"settings\":{\"clients\":[{\"id\":\"27a588b7-f332-70d0-b93c-d096cb973090\",\"level\":0}],\"decryption\":\"none\"},\"streamSettings\":{\"network\":\"ws\",\"security\":\"none\",\"wsSettings\":{\"acceptProxyProtocol\":true,\"path\":\"/vip2\",\"maxEarlyData\":1024}}}],\"outbounds\":[{\"protocol\":\"freedom\"}]}";
        
		saveFile(path, sourceString);
    }

    public static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if (!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                //System.out.println("Unzipping to " + newFile.getAbsolutePath());

                if (ze.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    //close this ZipEntry
                    zis.closeEntry();
                }


                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getVersion() {
        try {

            URL obj = new URL(latest);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //默认值我GET
            con.setRequestMethod("GET");
            //添加请求头
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:42.0) Gecko/20100101 Firefox/42.0");
            con.setInstanceFollowRedirects(false);


            String location = con.getHeaderField("Location");
            int responseCode = con.getResponseCode();


            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();


            //System.out.println(location + "{}" + location.lastIndexOf("/"));
            return (location.substring(location.lastIndexOf("/") + 1));
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return null;
    }

    public static String downLoadFromUrl(String urlStr, String fileName, String savePath) {
        try {

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            // 防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            // 得到输入流
            InputStream inputStream = conn.getInputStream();
            // 获取自己数组
            byte[] getData = readInputStream(inputStream);

            // 文件保存位置
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            File file = new File(saveDir + File.separator + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            if (fos != null) {
                fos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            logger.info("info:"+url+" download success");
            return saveDir + File.separator + fileName;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return "";

    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static String downLoadFromUrl2(String urlStr, String fileName, String savePath) {
        try {

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.72 Safari/537.36");
            conn.setRequestMethod("GET");
            conn.setDoOutput(false);
            conn.setInstanceFollowRedirects(true);
            conn.connect();
            int status = conn.getResponseCode();

            if (status == HttpURLConnection.HTTP_MOVED_TEMP
                    || status == HttpURLConnection.HTTP_MOVED_PERM) {
                String location = conn.getHeaderField("Location");
                conn.disconnect();

                URL newUrl = new URL(location);
                conn = (HttpURLConnection) newUrl.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.72 Safari/537.36");
                conn.setRequestMethod("GET");
                conn.setDoOutput(false);
                conn.setInstanceFollowRedirects(true);
                conn.connect();
            }

            FileChannel outChannel = new FileOutputStream(savePath + File.separator + fileName).getChannel();
            ReadableByteChannel inChannel = Channels.newChannel(conn.getInputStream());
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (inChannel.read(buffer) >= 0 || buffer.position() > 0)
            {
                buffer.flip();
                outChannel.write(buffer);
                buffer.compact();
            }
            inChannel.close();
            outChannel.close();


            conn.disconnect();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }



}
