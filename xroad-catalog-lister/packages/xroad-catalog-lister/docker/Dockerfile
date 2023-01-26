FROM centos:7
MAINTAINER "Sami Kallio" <sami.kallio@gofore.com>

RUN yum -y install sudo git rpm-build java-11-openjdk-headless
RUN yum clean all
RUN sed -i 's/requiretty/!requiretty/' /etc/sudoers

ARG CATALOG_PROFILE="default"
USER root
RUN echo "Chosen catalog profile = $CATALOG_PROFILE"
CMD ["sh", "-c", "/workspace/xroad-catalog-lister/build_rpm.sh -p ${CATALOG_PROFILE}" ]
