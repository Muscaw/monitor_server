package dev.muscaw.monitor.transport.api;

import dev.muscaw.monitor.util.domain.LatLon;
import dev.muscaw.monitor.util.domain.WallClock;
import retrofit2.Retrofit;

import java.text.SimpleDateFormat;

public class PublicTransportNextDeparturesServiceImpl {

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
}
