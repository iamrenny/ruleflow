FROM mongo:3.6.20-xenial

COPY ensure_indexes.js .

ENTRYPOINT [ "sh", "-c", "mongo $DOCUMENTDB_CONN_STRING_WRITE ensure_indexes.js" ]