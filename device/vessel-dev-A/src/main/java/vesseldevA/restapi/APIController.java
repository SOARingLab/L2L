package vesseldevA.restapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.security.krb5.internal.crypto.Des;
import vesseldevA.domain.Destination;
import vesseldevA.services.shadow.VesselDevice;

import java.io.IOException;
import java.util.List;

@RestController
public class APIController {
    private static final Logger logger = LoggerFactory.getLogger(APIController.class);
    @Autowired
    private VesselDevice vesselDevice;
    @RequestMapping("/hello")
    String home() {
        logger.info("test rest api.");
        return "hello , vessel-dev-A";
    }

    @RequestMapping(value = "/delay" , method = RequestMethod.POST , produces = "application/json")
    public ResponseEntity<List<Destination>> delay(@RequestBody  List<Destination> newDestinations){
        logger.debug("--delay--"+newDestinations.toString());
        List<Destination> oldDestinations = vesselDevice.getDestinations();
        for(int i = 0 ; i < newDestinations.size();i++){
            Destination oldDest = oldDestinations.get(i);
            Destination newDest = newDestinations.get(i);
            oldDest.setEstiAnchorTime(newDest.getEstiAnchorTime());
            oldDest.setEstiArrivalTime(newDest.getEstiArrivalTime());
            oldDest.setEstiDepartureTime(newDest.getEstiDepartureTime());
        }
        return new ResponseEntity<List<Destination>>(newDestinations , HttpStatus.OK);
    }

    @RequestMapping(value = "/status/{status}" , method = RequestMethod.POST , produces = "application/json")
    public ResponseEntity<String> updateStatus(@PathVariable("status") String status){
        logger.debug("--delay--"+status);
        vesselDevice.updateStatus(status);
        return new ResponseEntity<String>(status , HttpStatus.OK);
    }
}
