First of all to link the frontend with the inventory-service you have to make sure to install this:
npm install @openapitools/openapi-generator-cli -g

npm update -g @openapitools/openapi-generator-cli


and we will reuse the openapi generator from the inventory service for our frontend:
openapi-generator generate \
-i ./inventory-service/open_api/openapi.yaml \
-g kotlin \
-o ./android-frontend/generated/inventory-client \
--additional-properties=library=multiplatform,serializationLibrary=gson
