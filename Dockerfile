# We need this docker file to run backend and frontend applications in a single container.
# Openshift allows you to run a single container for free.

FROM java:8

MAINTAINER Bahadir Yilmaz

ADD ./kalah-backend/target/kalah-backend-1.0-SNAPSHOT.jar kalah-backend-1.0-SNAPSHOT.jar
ADD ./kalah-frontend/target/kalah-frontend-1.0-SNAPSHOT.jar kalah-frontend-1.0-SNAPSHOT.jar
ADD start-script-for-docker.sh start.sh

CMD ["bash", "start.sh"]
