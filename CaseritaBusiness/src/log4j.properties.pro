# Log4j configuration file.
log4j.rootCategory=INFO,A4,stdout

#
# stdout is ConsoleAppender
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
# Pattern to output the caller's file name and line number.
log4j.appender.stdout.layout.ConversionPattern=[%d] [%C{1}.%M(%L)] - %-5p - %m%n

#
# A4 is a DailyRollingFileAppender
log4j.appender.A4=org.apache.log4j.DailyRollingFileAppender
log4j.appender.A4.file=/home/ServicioIntegracionWMS/logs/logIntegracionWMS.log
log4j.appender.A4.MaxFileSize=60000KB

#
# Keep three backup files.
log4j.appender.A4.MaxBackupIndex=10
log4j.appender.A4.datePattern='.'yyyy-MM-dd
log4j.appender.A4.append=true
log4j.appender.A4.layout=org.apache.log4j.PatternLayout
log4j.appender.A4.layout.ConversionPattern=[%d] [%c.%M(%L)] - %-5p - %m%n
