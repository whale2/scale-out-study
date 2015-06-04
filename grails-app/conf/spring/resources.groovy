import tstapp.CouchBaseConnectorService
import TopApps

// Place your Spring DSL code here
beans = {
    couchBucket(CouchBaseConnectorService)
    fillBuckets(FillBuckets) { bean ->
        bean.autowire = 'byName'
    }
    topApps(TopApps) { bean ->
        bean.autowire = 'byName'
    }
}
