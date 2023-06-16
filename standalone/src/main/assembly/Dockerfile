FROM nexus3.onap.org:10001/onap/integration-java11:9.0.0

USER root

RUN mkdir /home/uui

ADD usecase-ui-intent-analysis-*-linux64.tar.gz /home/uui/

WORKDIR /home/uui

EXPOSE 8083
USER uui
WORKDIR /home/uui
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar usecase-ui-intent-analysis.jar"]
