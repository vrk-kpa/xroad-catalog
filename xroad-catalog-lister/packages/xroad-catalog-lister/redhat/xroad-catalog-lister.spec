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

%define src .
%define jlib /usr/lib/xroad-catalog

%description
X-Road service listing

%prep

%build

%install
cp -p ../../build/libs/xroad-catalog-lister.jar %{buildroot}%{jlib}
cp -p %{src}/%{name}.service %{buildroot}%{_unitdir}
cp -p %{src}/%{name} %{buildroot}/usr/share/xroad/bin

%clean
rm -rf %{buildroot}

%files
%attr(644,root,root) %{_unitdir}/xroad-catalog-lister.service
%{jlib}/xroad-catalog-lister.jar
%attr(744,xroad,xroad) /usr/share/xroad/bin/%{name}

%post
%systemd_post xroad-catalog-lister.service

%preun
%systemd_preun xroad-catalog-lister.service

%postun
%systemd_postun_with_restart xroad-catalog-lister.service

%changelog

