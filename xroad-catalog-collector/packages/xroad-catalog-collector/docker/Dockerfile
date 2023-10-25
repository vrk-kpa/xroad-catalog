FROM centos:7
MAINTAINER "Nordic Institute for Interoperability Solutions (NIIS)" <x-road@niis.org>

RUN yum -y install sudo git rpm-build java-11-openjdk-headless
RUN yum clean all
RUN sed -i 's/requiretty/!requiretty/' /etc/sudoers

ARG CATALOG_PROFILE
ENV CATALOG_PROFILE=${CATALOG_PROFILE}
USER root
CMD /workspace/xroad-catalog-collector/build_rpm.sh -p $CATALOG_PROFILE

