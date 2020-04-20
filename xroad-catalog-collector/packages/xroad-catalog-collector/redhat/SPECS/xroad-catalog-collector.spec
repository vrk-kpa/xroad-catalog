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
Requires:           systemd, cronie, cronie-anacron, postgresql, postgresql-server, java-1.8.0-openjdk
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
mkdir -p %{buildroot}%{conf}
mkdir -p %{buildroot}%{_unitdir}
#mkdir -p %{buildroot}/etc/cron.d
mkdir -p %{buildroot}/usr/share/xroad/bin
mkdir -p %{buildroot}/usr/share/xroad/sql
mkdir -p %{buildroot}/var/log/xroad/
cp -p %{src}/../../../build/libs/xroad-catalog-collector-%{version}.jar %{buildroot}%{jlib}/%{name}.jar
cp -p %{src}/../../../build/resources/main/collector-production.properties %{buildroot}%{conf}
cp -p %{src}/../../../build/resources/main/catalogdb-production.properties %{buildroot}%{conf}
cp -p %{src}/../../../build/resources/main/application.conf %{buildroot}%{conf}
cp -p  ../../../../../xroad-catalog-persistence/src/main/sql/*.sql %{buildroot}/usr/share/xroad/sql
#cp -p %{src}/SOURCES/%{name}.cron %{buildroot}/etc/cron.d/%{name}
cp -p %{src}/SOURCES/%{name} %{buildroot}/usr/share/xroad/bin
cp -p %{src}/SOURCES/%{name}.service %{buildroot}%{_unitdir}

%clean
rm -rf %{buildroot}

%files
%defattr(600,xroad-catalog,xroad-catalog,-)
%config(noreplace) %{conf}/application.conf
%config(noreplace) %{conf}/collector-production.properties
%config(noreplace) %{conf}/catalogdb-production.properties

%attr(644,root,root) /usr/share/xroad/sql/init_database.sql
%attr(644,root,root) /usr/share/xroad/sql/create_tables.sql
#%attr(755,root,root) /etc/cron.d/%{name}
%attr(644,root,root) %{_unitdir}/%{name}.service
%attr(744,xroad-catalog,xroad-catalog) %{jlib}/%{name}.jar
%attr(744,xroad-catalog,xroad-catalog) /usr/share/xroad/bin/%{name}

%pre

if ! id xroad-catalog > /dev/null 2>&1 ; then
    adduser --system --no-create-home --shell /bin/false xroad-catalog
fi

%post

PGSETUP_INITDB_OPTIONS="--auth-host=md5 -E UTF8" postgresql-setup initdb || return 1
systemctl start postgresql

#Check if database was already initialized
if sudo -u postgres psql -lqt |cut -d \| -f 1 | grep -qw xroad_catalog ; then
    sudo -u postgres psql --file=/usr/share/xroad/sql/create_tables.sql
else
    echo "Initializing database and creating tables"
    sudo -u postgres psql --file=/usr/share/xroad/sql/init_database.sql
    sudo -u postgres psql --file=/usr/share/xroad/sql/create_tables.sql
fi


%systemd_post %{name}.service

%preun
%systemd_preun %{name}.service

%postun
%systemd_postun_with_restart %{name}.service


%changelog

