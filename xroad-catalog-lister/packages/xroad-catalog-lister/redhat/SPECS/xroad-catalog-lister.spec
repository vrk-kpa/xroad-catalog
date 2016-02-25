# do not repack jars
%define __jar_repack %{nil}
# produce .elX dist tag on both centos and redhat
%define dist %(/usr/lib/rpm/redhat/dist.sh)

Name:               xroad-catalog-lister
Version:            0.1.0
Release:            1
Summary:            X-Road Service Listing
Group:              Applications/Internet
License:            MIT
Requires:           systemd
Requires(post):     systemd
Requires(preun):    systemd
Requires(postun):   systemd

%define src %{_topdir}
%define jlib /usr/lib/xroad-catalog

%description
X-Road service listing

%prep

%build

%install
mkdir -p %{buildroot}%{jlib}
mkdir -p %{buildroot}%{_unitdir}
mkdir -p %{buildroot}/usr/share/xroad/bin
cp -p %{src}/../../../build/libs/xroad-catalog-lister.jar %{buildroot}%{jlib}
cp -p %{src}/SOURCES/%{name}.service %{buildroot}%{_unitdir}
cp -p %{src}/SOURCES/%{name} %{buildroot}/usr/share/xroad/bin

%clean
rm -rf %{buildroot}

%files
%attr(644,root,root) %{_unitdir}/xroad-catalog-lister.service
%{jlib}/xroad-catalog-lister.jar
%attr(744,root,root) /usr/share/xroad/bin/%{name}

%post
%systemd_post xroad-catalog-lister.service
if ! id xroad-catalog > /dev/null 2>&1 ; then
    adduser --system --no-create-home --shell /bin/false xroad-catalog
fi                
chmod 755 /usr/lib/xroad-catalog/xroad-catalog-lister.jar
chown -R xroad-catalog:xroad-catalog /usr/lib/xroad-catalog

%preun
%systemd_preun xroad-catalog-lister.service

%postun
%systemd_postun_with_restart xroad-catalog-lister.service

%changelog

