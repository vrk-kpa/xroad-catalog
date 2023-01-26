FROM centos:7
MAINTAINER "Sami Kallio" <sami.kallio@gofore.com>

ARG CATALOG_PROFILE=default

RUN yum -y install sudo git rpm-build java-11-openjdk-headless
RUN yum clean all
RUN sed -i 's/requiretty/!requiretty/' /etc/sudoers

ARG CATALOG_PROFILE
USER root
RUN echo ${CATALOG_PROFILE}
CMD ["sh", "-c", "/workspace/xroad-catalog-collector/build_rpm.sh -p ${CATALOG_PROFILE}" ]

