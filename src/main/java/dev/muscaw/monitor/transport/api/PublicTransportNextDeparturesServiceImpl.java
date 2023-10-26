package dev.muscaw.monitor.transport.api;

import dev.muscaw.monitor.transport.domain.Departure;
import dev.muscaw.monitor.transport.domain.PublicTransportNextDeparturesService;
import dev.muscaw.monitor.util.domain.LatLon;
import dev.muscaw.monitor.util.domain.WallClock;
import org.apache.batik.dom.util.DocumentFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import retrofit2.Retrofit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class PublicTransportNextDeparturesServiceImpl implements PublicTransportNextDeparturesService {

    private record Stop(String name, String didok) {}

    public static final int MAX_RETRIES = 3;
    private WallClock wallClock;
    private final String token;
    private final TriasDepartureEndpoint client;

    public PublicTransportNextDeparturesServiceImpl(WallClock wallClock, String baseUrl, String token) {
        this.wallClock = wallClock;
        this.token = token;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .build();

        client = retrofit.create(TriasDepartureEndpoint.class);
    }

    private String getCurrentDateFormatted() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return formatter.format(wallClock.now());
    }

    private String generateLocationInformationRequest(LatLon location) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Trias version=\"1.1\" xmlns=\"http://www.vdv.de/trias\" xmlns:siri=\"http://www.siri.org.uk/siri\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.vdv.de/trias ../trias-xsd-v1.1/Trias.xsd\">\n" +
                "    <ServiceRequest>\n" +
                "        <siri:RequestTimestamp>" + getCurrentDateFormatted() + "</siri:RequestTimestamp>\n" +
                "        <siri:RequestorRef>MMonitor</siri:RequestorRef>\n" +
                "        <RequestPayload>\n" +
                "            <LocationInformationRequest>\n" +
                "                <InitialInput>\n" +
                "                    <GeoPosition>\n" +
                "                        <Longitude>" + location.lon() + "</Longitude>\n" +
                "                        <Latitude>" + location.lat() + "</Latitude>\n" +
                "                    </GeoPosition>\n" +
                "                </InitialInput>\n" +
                "                <Restrictions>\n" +
                "                    <Type>stop</Type>\n" +
                "                    <NumberOfResults>11</NumberOfResults>\n" +
                "                    <IncludePtModes>false</IncludePtModes>\n" +
                "                </Restrictions>\n" +
                "            </LocationInformationRequest>\n" +
                "        </RequestPayload>\n" +
                "    </ServiceRequest>\n" +
                "</Trias>";
    }

    @Override
    public Departure getNextDepartureForLocation(LatLon location) {
        return retryGetNextDepartureForLocation(location, MAX_RETRIES);
    }

    private Departure retryGetNextDepartureForLocation(LatLon location, int retriesLeft) {

        CompletableFuture<String> result = client.getTriasResult(generateLocationInformationRequest(location));
        try {
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            if (retriesLeft > 0) {
                return retryGetNextDepartureForLocation(location, retriesLeft - 1);
            } else {
                // After we went through all retries, we throw
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private List<Stop> parseStops(String response) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        ByteArrayInputStream bis = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
        Document doc = builder.parse(bis);
        Element root = doc.getDocumentElement();
        var locationInformationResponse = root.getElementsByTagName("Location");
        for(int i = 0; i < locationInformationResponse.getLength(); i++) {
            var location = locationInformationResponse.item(i);
        }
    }
}
