# do not repack jars
%define __jar_repack %{nil}
# produce .elX dist tag on both centos and redhat
%define dist %(/usr/lib/rpm/redhat/dist.sh)

Name:               xroad-catalog-collector
Version:            0.1.0
Release:            1
Summary:            X-Road Service Listing
Group:              Applications/Internet
License:            MIT
Requires:           systemd
Requires(post):     systemd
Requires(preun):    systemd
Requires(postun):   systemd
Requires:           vixie-cron, postgresql

%define src %{_topdir}
%define jlib /usr/lib/xroad-catalog

%description
X-Road service listing

%prep

%build

%install
mkdir -p %{buildroot}%{jlib}
mkdir -p %{buildroot}%{_unitdir}
mkdir -p %{buildroot}/etc/cron.daily
mkdir -p %{buildroot}/usr/share/xroad/bin
cp -p %{src}/../../../build/libs/xroad-catalog-collector.jar %{buildroot}%{jlib}
cp -p %{src}/SOURCES/%{name}.cron %{buildroot}/etc/cron.daily/%{name}
cp -p %{src}/SOURCES/%{name} %{buildroot}/usr/share/xroad/bin

%clean
rm -rf %{buildroot}

%files
%attr(644,root,root) /etc/cron.daily/%{name}
%attr(755,xroad-catalog,xroad-catalog) %{jlib}/xroad-catalog-collector.jar
%attr(744,xroad-catalog,xroad-catalog) /usr/share/xroad/bin/%{name}

%pre
if ! id xroad-catalog > /dev/null 2>&1 ; then
    adduser --system --no-create-home --shell /bin/false xroad-catalog
fi

%post
#Init database

%preun


%postun


%changelog

