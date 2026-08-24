package de.mm20.launcher2.locations

import de.mm20.launcher2.locations.providers.openstreetmaps.OsmLocation
import de.mm20.launcher2.locations.providers.openstreetmaps.OsmLocationProvider
import de.mm20.launcher2.search.SavableSearchable
import de.mm20.launcher2.search.SearchableDeserializer
import de.mm20.launcher2.search.SearchableSerializer
import de.mm20.launcher2.search.location.Address
import de.mm20.launcher2.search.location.Attribution
import de.mm20.launcher2.search.location.Departure
import de.mm20.launcher2.search.location.LocationIcon
import de.mm20.launcher2.search.location.OpeningSchedule
import de.mm20.launcher2.search.location.PaymentMethod
import de.mm20.launcher2.serialization.Json
import kotlinx.serialization.Serializable

@Serializable
internal data class SerializedLocation(
    val id: String? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    val icon: LocationIcon? = null,
    val category: String? = null,
    val label: String? = null,
    val address: Address? = null,
    val websiteUrl: String? = null,
    val phoneNumber: String? = null,
    val emailAddress: String? = null,
    val userRating: Float? = null,
    val userRatingCount: Int? = null,
    val openingSchedule: OpeningSchedule? = null,
    val timestamp: Long? = null,
    val departures: List<Departure>? = null,
    val fixMeUrl: String? = null,
    val attribution: Attribution? = null,
    val acceptedPaymentMethods: Map<PaymentMethod, Boolean>? = null,
)

internal class OsmLocationSerializer : SearchableSerializer {
    override fun serialize(searchable: SavableSearchable): String {
        searchable as OsmLocation
        return Json.Lenient.encodeToString(
            SerializedLocation(
                id = searchable.id.toString(),
                lat = searchable.latitude,
                lon = searchable.longitude,
                icon = searchable.icon,
                category = searchable.category,
                label = searchable.label,
                address = searchable.address,
                websiteUrl = searchable.websiteUrl,
                phoneNumber = searchable.phoneNumber,
                emailAddress = searchable.emailAddress,
                userRating = searchable.userRating,
                userRatingCount = searchable.userRatingCount,
                openingSchedule = searchable.openingSchedule,
                timestamp = searchable.timestamp,
                departures = searchable.departures,
                fixMeUrl = searchable.fixMeUrl,
                acceptedPaymentMethods = searchable.acceptedPaymentMethods
            )
        )
    }

    override val typePrefix: String
        get() = "osmlocation"
}

internal class OsmLocationDeserializer(
    private val osmProvider: OsmLocationProvider,
) : SearchableDeserializer {
    override suspend fun deserialize(serialized: String): SavableSearchable? {
        val json = Json.Lenient.decodeFromString<SerializedLocation>(serialized)
        val id = json.id?.toLongOrNull() ?: return null

        return OsmLocation(
            id = id,
            latitude = json.lat ?: return null,
            longitude = json.lon ?: return null,
            icon = json.icon,
            category = json.category,
            label = json.label ?: return null,
            address = json.address,
            websiteUrl = json.websiteUrl,
            phoneNumber = json.phoneNumber,
            emailAddress = json.emailAddress,
            userRating = json.userRating,
            openingSchedule = json.openingSchedule,
            timestamp = json.timestamp ?: return null,
            acceptedPaymentMethods = json.acceptedPaymentMethods,
            updatedSelf = {
                osmProvider.update(id)
            }
        )
    }
}
