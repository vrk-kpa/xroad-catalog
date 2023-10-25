/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS)
 * Copyright (c) 2016-2023 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.lister;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequestMapping("/default")
@Profile({"default", "fi"})
public interface ServiceOperations {

    @GetMapping(path = {"/listErrors/{xRoadInstance}/{memberClass}/{memberCode}/{subsystemCode}",
                        "/listErrors/{xRoadInstance}/{memberClass}/{memberCode}",
                        "/listErrors/{xRoadInstance}/{memberClass}",
                        "/listErrors/{xRoadInstance}",
                        "/listErrors"},
               produces = "application/json")
    ResponseEntity<?> listErrors(@PathVariable(required = false) String xRoadInstance,
                                 @PathVariable(required = false) String memberClass,
                                 @PathVariable(required = false) String memberCode,
                                 @PathVariable(required = false) String subsystemCode,
                                 @RequestParam(required = false) String startDate,
                                 @RequestParam(required = false) String endDate,
                                 @RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer limit);

    @GetMapping(path = "/getDistinctServiceStatistics", produces = "application/json")
    ResponseEntity<?> getDistinctServiceStatistics(@RequestParam(required = false) String startDate,
                                                   @RequestParam(required = false) String endDate);

    @GetMapping(path = "/getServiceStatistics", produces = "application/json")
    ResponseEntity<?> getServiceStatistics(@RequestParam(required = false) String startDate,
                                           @RequestParam(required = false) String endDate);

    @GetMapping(path = "/getServiceStatisticsCSV", produces = "text/csv")
    ResponseEntity<?> getServiceStatisticsCSV(@RequestParam(required = false) String startDate,
                                              @RequestParam(required = false) String endDate);

    @GetMapping(path = "/getListOfServices", produces = "application/json")
    ResponseEntity<?> getListOfServices(@RequestParam(required = false) String startDate,
                                        @RequestParam(required = false) String endDate);

    @GetMapping(path = "/getListOfServicesCSV", produces = "text/csv")
    ResponseEntity<?> getListOfServicesCSV(@RequestParam(required = false) String startDate,
                                           @RequestParam(required = false) String endDate);

    @GetMapping(path = "/listSecurityServers", produces = "application/json")
    ResponseEntity<?> listSecurityServers();

    @GetMapping(path = "/listDescriptors", produces = "application/json")
    ResponseEntity<?> listDescriptors();

    @GetMapping(path = "/getEndpoints/{xRoadInstance}/{memberClass}/{memberCode}/{subsystemCode}/{serviceCode}", produces = "application/json")
    ResponseEntity<?> getEndpoints(@PathVariable String xRoadInstance,
                                   @PathVariable String memberClass,
                                   @PathVariable String memberCode,
                                   @PathVariable String subsystemCode,
                                   @PathVariable String serviceCode);

    @GetMapping(path = "/getRest/{xRoadInstance}/{memberClass}/{memberCode}/{subsystemCode}/{serviceCode}", produces = "application/json")
    ResponseEntity<?> getRest(@PathVariable String xRoadInstance,
                              @PathVariable String memberClass,
                              @PathVariable String memberCode,
                              @PathVariable String subsystemCode,
                              @PathVariable String serviceCode);
}
