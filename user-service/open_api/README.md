# Modifying the openapi.yaml file

To modify or extend the API functionality, you will need to update the openapi.yaml file. Below are the key sections you might want to modify.

## 1. Defining Paths (Endpoints)

Paths represent the available API endpoints. Each path is associated with HTTP operations (e.g., GET, POST, PUT, DELETE) and contains metadata, parameters, request body details, and responses. You can find the definition of a certain endpoint in its corresponding domain folder. If you want to create a endpoint for a new domain, you need to reference it in the openapi.yaml document.

Example:
### item/endpoint.yaml
```yaml
/items:
  get:
    summary: Retrieve all items
    operationId: getItems
    responses:
      '200':
        description: A list of items
        content:
          'application/json':
            schema:
              type: array
              items:
                $ref: './component.yaml#/Item'
  post:
    summary: Create a new item
    operationId: createItem
    requestBody:
      content:
        'application/json':
          schema:
            $ref: './component.yaml#/NewItem'
    responses:
      '201':
        description: Item created
        content:
          'application/json':
            schema:
              $ref: './component.yaml#/Item'
```

### openapi.yaml
```yaml
paths:
  /items:
      $ref: './item/endpoint.yaml#/%2Fitem'
```
To reference a endpoint containing a "/" in an external file, you need to url-encode the "/", making it "%2F"

## 2. Modifying Request and Response Schemas

The schema definitions under components/schemas represent the structure of your request and response bodies.

Example:
### user/component.yaml
```yaml
schemas:
  Item:
    type: object
    properties:
      id:
        type: integer
      name:
        type: string
      email:
        type: string
  NewItem:
    type: object
    properties:
      name:
        type: string
      email:
        type: string

```

## 3. Adding Parameters

Parameters can be defined at the path or operation level. They can be query parameters, path parameters, headers, or cookies.

Example:
```yaml
paths:
  /items/{itemId}:
    get:
      summary: Retrieve a specific item
      parameters:
        - name: itemId
          in: path
          required: true
          schema:
            type: integer
      responses:
        '200':
          description: A single item
          content:
            'application/json':
              schema:
                $ref: './component.yaml#/Item'

```

## How to update files
To see how to regenerate the files after updating the docs, check out the service read_me

## How to access the generated Files
When you generate the files with a:
```shell
mvn install
```
You should make sure that you add the folders in which the files are generated to the source folders.
Go to Project structure and then to modules and add the generated folders to the source folders.
You can find the generated folders and files in the target/generated-sources/openapi/......