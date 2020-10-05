import amf.client.AMF;
import amf.client.model.document.BaseUnit;
import amf.client.model.document.DialectInstance;
import amf.client.model.document.Document;
import amf.client.parse.*;
import amf.core.remote.AsyncApi;
import amf.core.remote.AsyncApi20;
import amf.plugins.document.webapi.parser.spec.async.AsyncApi20DocumentParser;

import java.util.concurrent.ExecutionException;

import static java.lang.System.out;

public class example {

    public static String streetlights = "{\n" +
            "   \"asyncapi\": \"2.0.0\",\n" +
            "   \"info\": {\n" +
            "      \"title\": \"Streetlights API\",\n" +
            "      \"version\": \"1.0.0\",\n" +
            "      \"description\": \"The Smartylighting Streetlights API allows you to remotely manage the city lights.\\n### Check out its awesome features:\\n* Turn a specific streetlight on/off \uD83C\uDF03\\n* Dim a specific streetlight \uD83D\uDE0E\\n* Receive real-time information about environmental lighting conditions \uD83D\uDCC8\\n\",\n" +
            "      \"license\": {\n" +
            "         \"name\": \"Apache 2.0\",\n" +
            "         \"url\": \"https://www.apache.org/licenses/LICENSE-2.0\"\n" +
            "      }\n" +
            "   },\n" +
            "   \"servers\": {\n" +
            "      \"production\": {\n" +
            "         \"url\": \"test.mosquitto.org:{port}\",\n" +
            "         \"protocol\": \"mqtt\",\n" +
            "         \"description\": \"Test broker\",\n" +
            "         \"variables\": {\n" +
            "            \"port\": {\n" +
            "               \"description\": \"Secure connection (TLS) is available through port 8883.\",\n" +
            "               \"default\": \"1883\",\n" +
            "               \"enum\": [\n" +
            "                  \"1883\",\n" +
            "                  \"8883\"\n" +
            "               ]\n" +
            "            }\n" +
            "         },\n" +
            "         \"security\": [\n" +
            "            {\n" +
            "               \"apiKey\": []\n" +
            "            },\n" +
            "            {\n" +
            "               \"supportedOauthFlows\": [\n" +
            "                  \"streetlights:on\",\n" +
            "                  \"streetlights:off\",\n" +
            "                  \"streetlights:dim\"\n" +
            "               ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"openIdConnectWellKnown\": []\n" +
            "            }\n" +
            "         ]\n" +
            "      }\n" +
            "   },\n" +
            "   \"defaultContentType\": \"application/json\",\n" +
            "   \"channels\": {\n" +
            "      \"smartylighting/streetlights/1/0/event/{streetlightId}/lighting/measured\": {\n" +
            "         \"description\": \"The topic on which measured values may be produced and consumed.\",\n" +
            "         \"parameters\": {\n" +
            "            \"streetlightId\": {\n" +
            "               \"$ref\": \"#/components/parameters/streetlightId\"\n" +
            "            }\n" +
            "         },\n" +
            "         \"publish\": {\n" +
            "            \"summary\": \"Inform about environmental lighting conditions of a particular streetlight.\",\n" +
            "            \"operationId\": \"receiveLightMeasurement\",\n" +
            "            \"traits\": [\n" +
            "               {\n" +
            "                  \"$ref\": \"#/components/operationTraits/kafka\"\n" +
            "               }\n" +
            "            ],\n" +
            "            \"message\": {\n" +
            "               \"$ref\": \"#/components/messages/lightMeasured\"\n" +
            "            }\n" +
            "         }\n" +
            "      },\n" +
            "      \"smartylighting/streetlights/1/0/action/{streetlightId}/turn/on\": {\n" +
            "         \"parameters\": {\n" +
            "            \"streetlightId\": {\n" +
            "               \"$ref\": \"#/components/parameters/streetlightId\"\n" +
            "            }\n" +
            "         },\n" +
            "         \"subscribe\": {\n" +
            "            \"operationId\": \"turnOn\",\n" +
            "            \"traits\": [\n" +
            "               {\n" +
            "                  \"$ref\": \"#/components/operationTraits/kafka\"\n" +
            "               }\n" +
            "            ],\n" +
            "            \"message\": {\n" +
            "               \"$ref\": \"#/components/messages/turnOnOff\"\n" +
            "            }\n" +
            "         }\n" +
            "      },\n" +
            "      \"smartylighting/streetlights/1/0/action/{streetlightId}/turn/off\": {\n" +
            "         \"parameters\": {\n" +
            "            \"streetlightId\": {\n" +
            "               \"$ref\": \"#/components/parameters/streetlightId\"\n" +
            "            }\n" +
            "         },\n" +
            "         \"subscribe\": {\n" +
            "            \"operationId\": \"turnOff\",\n" +
            "            \"traits\": [\n" +
            "               {\n" +
            "                  \"$ref\": \"#/components/operationTraits/kafka\"\n" +
            "               }\n" +
            "            ],\n" +
            "            \"message\": {\n" +
            "               \"$ref\": \"#/components/messages/turnOnOff\"\n" +
            "            }\n" +
            "         }\n" +
            "      },\n" +
            "      \"smartylighting/streetlights/1/0/action/{streetlightId}/dim\": {\n" +
            "         \"parameters\": {\n" +
            "            \"streetlightId\": {\n" +
            "               \"$ref\": \"#/components/parameters/streetlightId\"\n" +
            "            }\n" +
            "         },\n" +
            "         \"subscribe\": {\n" +
            "            \"operationId\": \"dimLight\",\n" +
            "            \"traits\": [\n" +
            "               {\n" +
            "                  \"$ref\": \"#/components/operationTraits/kafka\"\n" +
            "               }\n" +
            "            ],\n" +
            "            \"message\": {\n" +
            "               \"$ref\": \"#/components/messages/dimLight\"\n" +
            "            }\n" +
            "         }\n" +
            "      }\n" +
            "   },\n" +
            "   \"components\": {\n" +
            "      \"messages\": {\n" +
            "         \"lightMeasured\": {\n" +
            "            \"name\": \"lightMeasured\",\n" +
            "            \"title\": \"Light measured\",\n" +
            "            \"summary\": \"Inform about environmental lighting conditions of a particular streetlight.\",\n" +
            "            \"contentType\": \"application/json\",\n" +
            "            \"traits\": [\n" +
            "               {\n" +
            "                  \"$ref\": \"#/components/messageTraits/commonHeaders\"\n" +
            "               }\n" +
            "            ],\n" +
            "            \"payload\": {\n" +
            "               \"$ref\": \"#/components/schemas/lightMeasuredPayload\"\n" +
            "            }\n" +
            "         },\n" +
            "         \"turnOnOff\": {\n" +
            "            \"name\": \"turnOnOff\",\n" +
            "            \"title\": \"Turn on/off\",\n" +
            "            \"summary\": \"Command a particular streetlight to turn the lights on or off.\",\n" +
            "            \"traits\": [\n" +
            "               {\n" +
            "                  \"$ref\": \"#/components/messageTraits/commonHeaders\"\n" +
            "               }\n" +
            "            ],\n" +
            "            \"payload\": {\n" +
            "               \"$ref\": \"#/components/schemas/turnOnOffPayload\"\n" +
            "            }\n" +
            "         },\n" +
            "         \"dimLight\": {\n" +
            "            \"name\": \"dimLight\",\n" +
            "            \"title\": \"Dim light\",\n" +
            "            \"summary\": \"Command a particular streetlight to dim the lights.\",\n" +
            "            \"traits\": [\n" +
            "               {\n" +
            "                  \"$ref\": \"#/components/messageTraits/commonHeaders\"\n" +
            "               }\n" +
            "            ],\n" +
            "            \"payload\": {\n" +
            "               \"$ref\": \"#/components/schemas/dimLightPayload\"\n" +
            "            }\n" +
            "         }\n" +
            "      },\n" +
            "      \"schemas\": {\n" +
            "         \"lightMeasuredPayload\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "               \"lumens\": {\n" +
            "                  \"type\": \"integer\",\n" +
            "                  \"minimum\": 0,\n" +
            "                  \"description\": \"Light intensity measured in lumens.\"\n" +
            "               },\n" +
            "               \"sentAt\": {\n" +
            "                  \"$ref\": \"#/components/schemas/sentAt\"\n" +
            "               }\n" +
            "            }\n" +
            "         },\n" +
            "         \"turnOnOffPayload\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "               \"command\": {\n" +
            "                  \"type\": \"string\",\n" +
            "                  \"enum\": [\n" +
            "                     \"on\",\n" +
            "                     \"off\"\n" +
            "                  ],\n" +
            "                  \"description\": \"Whether to turn on or off the light.\"\n" +
            "               },\n" +
            "               \"sentAt\": {\n" +
            "                  \"$ref\": \"#/components/schemas/sentAt\"\n" +
            "               }\n" +
            "            }\n" +
            "         },\n" +
            "         \"dimLightPayload\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "               \"percentage\": {\n" +
            "                  \"type\": \"integer\",\n" +
            "                  \"description\": \"Percentage to which the light should be dimmed to.\",\n" +
            "                  \"minimum\": 0,\n" +
            "                  \"maximum\": 100\n" +
            "               },\n" +
            "               \"sentAt\": {\n" +
            "                  \"$ref\": \"#/components/schemas/sentAt\"\n" +
            "               }\n" +
            "            }\n" +
            "         },\n" +
            "         \"sentAt\": {\n" +
            "            \"type\": \"string\",\n" +
            "            \"format\": \"date-time\",\n" +
            "            \"description\": \"Date and time when the message was sent.\"\n" +
            "         }\n" +
            "      },\n" +
            "      \"securitySchemes\": {\n" +
            "         \"apiKey\": {\n" +
            "            \"type\": \"apiKey\",\n" +
            "            \"in\": \"user\",\n" +
            "            \"description\": \"Provide your API key as the user and leave the password empty.\"\n" +
            "         },\n" +
            "         \"supportedOauthFlows\": {\n" +
            "            \"type\": \"oauth2\",\n" +
            "            \"description\": \"Flows to support OAuth 2.0\",\n" +
            "            \"flows\": {\n" +
            "               \"implicit\": {\n" +
            "                  \"authorizationUrl\": \"https://authserver.example/auth\",\n" +
            "                  \"scopes\": {\n" +
            "                     \"streetlights:on\": \"Ability to switch lights on\",\n" +
            "                     \"streetlights:off\": \"Ability to switch lights off\",\n" +
            "                     \"streetlights:dim\": \"Ability to dim the lights\"\n" +
            "                  }\n" +
            "               },\n" +
            "               \"password\": {\n" +
            "                  \"tokenUrl\": \"https://authserver.example/token\",\n" +
            "                  \"scopes\": {\n" +
            "                     \"streetlights:on\": \"Ability to switch lights on\",\n" +
            "                     \"streetlights:off\": \"Ability to switch lights off\",\n" +
            "                     \"streetlights:dim\": \"Ability to dim the lights\"\n" +
            "                  }\n" +
            "               },\n" +
            "               \"clientCredentials\": {\n" +
            "                  \"tokenUrl\": \"https://authserver.example/token\",\n" +
            "                  \"scopes\": {\n" +
            "                     \"streetlights:on\": \"Ability to switch lights on\",\n" +
            "                     \"streetlights:off\": \"Ability to switch lights off\",\n" +
            "                     \"streetlights:dim\": \"Ability to dim the lights\"\n" +
            "                  }\n" +
            "               },\n" +
            "               \"authorizationCode\": {\n" +
            "                  \"authorizationUrl\": \"https://authserver.example/auth\",\n" +
            "                  \"tokenUrl\": \"https://authserver.example/token\",\n" +
            "                  \"refreshUrl\": \"https://authserver.example/refresh\",\n" +
            "                  \"scopes\": {\n" +
            "                     \"streetlights:on\": \"Ability to switch lights on\",\n" +
            "                     \"streetlights:off\": \"Ability to switch lights off\",\n" +
            "                     \"streetlights:dim\": \"Ability to dim the lights\"\n" +
            "                  }\n" +
            "               }\n" +
            "            }\n" +
            "         },\n" +
            "         \"openIdConnectWellKnown\": {\n" +
            "            \"type\": \"openIdConnect\",\n" +
            "            \"openIdConnectUrl\": \"https://authserver.example/.well-known\"\n" +
            "         }\n" +
            "      },\n" +
            "      \"parameters\": {\n" +
            "         \"streetlightId\": {\n" +
            "            \"description\": \"The ID of the streetlight.\",\n" +
            "            \"schema\": {\n" +
            "               \"type\": \"string\"\n" +
            "            }\n" +
            "         }\n" +
            "      },\n" +
            "      \"messageTraits\": {\n" +
            "         \"commonHeaders\": {\n" +
            "            \"headers\": {\n" +
            "               \"type\": \"object\",\n" +
            "               \"properties\": {\n" +
            "                  \"my-app-header\": {\n" +
            "                     \"type\": \"integer\",\n" +
            "                     \"minimum\": 0,\n" +
            "                     \"maximum\": 100\n" +
            "                  }\n" +
            "               }\n" +
            "            }\n" +
            "         }\n" +
            "      },\n" +
            "      \"operationTraits\": {\n" +
            "         \"kafka\": {\n" +
            "            \"bindings\": {\n" +
            "               \"kafka\": {\n" +
            "                  \"clientId\": \"my-app-id\"\n" +
            "               }\n" +
            "            }\n" +
            "         }\n" +
            "      }\n" +
            "   }\n" +
            "}";

    private static String petstore = "openapi: \"3.0.0\"\n" +
            "info:\n" +
            "  version: 1.0.0\n" +
            "  title: Swagger Petstore\n" +
            "  license:\n" +
            "    name: MIT\n" +
            "servers:\n" +
            "  - url: http://petstore.swagger.io/v1\n" +
            "paths:\n" +
            "  /pets:\n" +
            "    get:\n" +
            "      summary: List all pets\n" +
            "      operationId: listPets\n" +
            "      tags:\n" +
            "        - pets\n" +
            "      parameters:\n" +
            "        - name: limit\n" +
            "          in: query\n" +
            "          description: How many items to return at one time (max 100)\n" +
            "          required: false\n" +
            "          schema:\n" +
            "            type: integer\n" +
            "            format: int32\n" +
            "      responses:\n" +
            "        '200':\n" +
            "          description: A paged array of pets\n" +
            "          headers:\n" +
            "            x-next:\n" +
            "              description: A link to the next page of responses\n" +
            "              schema:\n" +
            "                type: string\n" +
            "          content:\n" +
            "            application/json:    \n" +
            "              schema:\n" +
            "                $ref: \"#/components/schemas/Pets\"\n" +
            "        default:\n" +
            "          description: unexpected error\n" +
            "          content:\n" +
            "            application/json:\n" +
            "              schema:\n" +
            "                $ref: \"#/components/schemas/Error\"\n" +
            "    post:\n" +
            "      summary: Create a pet\n" +
            "      operationId: createPets\n" +
            "      tags:\n" +
            "        - pets\n" +
            "      responses:\n" +
            "        '201':\n" +
            "          description: Null response\n" +
            "        default:\n" +
            "          description: unexpected error\n" +
            "          content:\n" +
            "            application/json:\n" +
            "              schema:\n" +
            "                $ref: \"#/components/schemas/Error\"\n" +
            "  /pets/{petId}:\n" +
            "    get:\n" +
            "      summary: Info for a specific pet\n" +
            "      operationId: showPetById\n" +
            "      tags:\n" +
            "        - pets\n" +
            "      parameters:\n" +
            "        - name: petId\n" +
            "          in: path\n" +
            "          required: true\n" +
            "          description: The id of the pet to retrieve\n" +
            "          schema:\n" +
            "            type: string\n" +
            "      responses:\n" +
            "        '200':\n" +
            "          description: Expected response to a valid request\n" +
            "          content:\n" +
            "            application/json:\n" +
            "              schema:\n" +
            "                $ref: \"#/components/schemas/Pet\"\n" +
            "        default:\n" +
            "          description: unexpected error\n" +
            "          content:\n" +
            "            application/json:\n" +
            "              schema:\n" +
            "                $ref: \"#/components/schemas/Error\"\n" +
            "components:\n" +
            "  schemas:\n" +
            "    Pet:\n" +
            "      type: object\n" +
            "      required:\n" +
            "        - id\n" +
            "        - name\n" +
            "      properties:\n" +
            "        id:\n" +
            "          type: integer\n" +
            "          format: int64\n" +
            "        name:\n" +
            "          type: string\n" +
            "        tag:\n" +
            "          type: string\n" +
            "    Pets:\n" +
            "      type: array\n" +
            "      items:\n" +
            "        $ref: \"#/components/schemas/Pet\"\n" +
            "    Error:\n" +
            "      type: object\n" +
            "      required:\n" +
            "        - code\n" +
            "        - message\n" +
            "      properties:\n" +
            "        code:\n" +
            "          type: integer\n" +
            "          format: int32\n" +
            "        message:\n" +
            "          type: string";

    //#parsing-example
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        AMF.init().get();

        //final BaseUnit result = new Oas30YamlParser().parseStringAsync(petstore).get();
        final BaseUnit result = new Async20Parser().parseStringAsync(streetlights).get();
        Document document = (Document) result;
        out.println(document.encodes().id());

    }


}
