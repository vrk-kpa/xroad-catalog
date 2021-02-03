# do not repack jars
%define __jar_repack %{nil}
# produce .elX dist tag on both centos and redhat
%define dist %(/usr/lib/rpm/redhat/dist.sh)

Name:               xroad-catalog-lister
Version:            %{xroad_catalog_version}
Release:            %{rel}%{?snapshot}%{?dist}
Summary:            X-Road Service Listing
Group:              Applications/Internet
License:            MIT
Requires:           systemd, java-1.8.0-openjdk
Requires(post):     systemd
Requires(preun):    systemd
Requires(postun):   systemd

%define src %{_topdir}
%define jlib /usr/lib/xroad-catalog
%define conf /etc/xroad/xroad-catalog

%description
X-Road service listing

%prep

%build

%install
mkdir -p %{buildroot}%{jlib}
mkdir -p %{buildroot}%{_unitdir}
mkdir -p %{buildroot}%{conf}
mkdir -p %{buildroot}/usr/share/xroad/bin
mkdir -p %{buildroot}/var/log/xroad/
cp -p %{src}/../../../build/libs/xroad-catalog-lister-%{version}.jar %{buildroot}%{jlib}/%{name}.jar
cp -p %{src}/SOURCES/%{name}.service %{buildroot}%{_unitdir}
cp -p %{src}/SOURCES/%{name} %{buildroot}/usr/share/xroad/bin
cp -p %{src}/../../../build/resources/main/lister-production.properties %{buildroot}%{conf}

%clean
rm -rf %{buildroot}

%files
%attr(644,root,root) %{_unitdir}/%{name}.service
%attr(755,xroad,xroad) %{jlib}/%{name}.jar
%attr(744,xroad,xroad) /usr/share/xroad/bin/%{name}
%config(noreplace) %{conf}/lister-production.properties
%attr(644,root,root) %{conf}/lister-production.properties

%pre
if ! id xroad-catalog > /dev/null 2>&1 ; then
    adduser --system --no-create-home --shell /bin/false xroad-catalog
fi

%post
%systemd_post %{name}.service

%preun
%systemd_preun %{name}.service

%postun
%systemd_postun_with_restart %{name}.service

%changelog

