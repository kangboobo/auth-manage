FROM cool-images-registry.cn-hangzhou.cr.aliyuncs.com/cool-public/centos7_jdk8:141zh_cn
MAINTAINER Fisher "gaofeng@coolcollege.cn"
VOLUME /tmp
ADD auth-manage-service/target/auth-manage-service-0.0.1-SNAPSHOT.jar /auth-manage-service.jar
RUN bash -c 'touch /auth-manage-service.jar'
ENV TZ 'Asia/Shanghai'
EXPOSE 30012
#RPC port
EXPOSE 12200
#debug port
EXPOSE 5005
ENTRYPOINT ["java","-jar","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005","/auth-manage-service.jar"]
