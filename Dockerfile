# Use an official Java runtime as a parent image
FROM java:8
# Set the working directory to /app
WORKDIR /app
# Copy the current directory contents into the container at /app
COPY . /app
# Run export sh
#RUN ./vita_path.sh
#Set evn
ENV TMPDIR=/app/Tools/netMHCpan-4.0/tmp
ENV NMHOME=/app/Tools/netMHCpan-4.0/
ENV NETMHCpan=/app/Tools/netMHCpan-4.0/
ENV NETCHOP=/app/Tools/netchop-3.1/
# Run vita
ENTRYPOINT ["java", "-jar", "/app/vita.jar"]
