# do not repack jars
%define __jar_repack %{nil}
# produce .elX dist tag on both centos and redhat
%define dist %(/usr/lib/rpm/redhat/dist.sh)

Name:               xroad-catalog-collector
Version:            %{xroad_catalog_version}
Release:            %{rel}%{?snapshot}%{?dist}
Summary:            X-Road Service Listing
Group:              Applications/Internet
License:            MIT
Requires:           systemd
Requires(post):     systemd
Requires(preun):    systemd
Requires(postun):   systemd
Requires:           cronie, cronie-anacron, postgresql

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
mkdir -p %{buildroot}/usr/share/xroad/sql
cp -p %{src}/../../../build/libs/xroad-catalog-collector.jar %{buildroot}%{jlib}
cp -p  ../../../../../xroad-catalog-persistence/src/main/sql/*.sql %{buildroot}/usr/share/xroad/sql
cp -p %{src}/SOURCES/%{name}.cron %{buildroot}/etc/cron.daily/%{name}
cp -p %{src}/SOURCES/%{name} %{buildroot}/usr/share/xroad/bin

%clean
rm -rf %{buildroot}

%files
%attr(644,root,root) /usr/share/xroad/sql/init_database.sql
%attr(644,root,root) /usr/share/xroad/sql/create_tables.sql
%attr(755,root,root) /etc/cron.daily/%{name}
%attr(755,xroad-catalog,xroad-catalog) %{jlib}/xroad-catalog-collector.jar
%attr(744,xroad-catalog,xroad-catalog) /usr/share/xroad/bin/%{name}

%pre
if ! id xroad-catalog > /dev/null 2>&1 ; then
    adduser --system --no-create-home --shell /bin/false xroad-catalog
fi

%post
#Init database
sudo -u postgres psql --file=/usr/share/xroad/sql/init_database.sql
sudo -u postgres psql --file=/usr/share/xroad/sql/create_tables.sql


%preun


%postun


%changelog

