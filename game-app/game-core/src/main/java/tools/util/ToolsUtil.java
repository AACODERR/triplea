package tools.util;

import java.awt.Point;
import java.awt.Polygon;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ToolsUtil {
  public static final String TERRITORY_SEA_ZONE_INFIX = "Sea Zone";

  /**
   * Finds a land territory name or some sea zone name where the point is contained in according to
   * the territory name -> polygons map.
   *
   * @param p A point on the map.
   * @param terrPolygons a map territory name -> polygons
   */
  public static Optional<String> findTerritoryName(
      final Point p, final Map<String, List<Polygon>> terrPolygons) {
    return Optional.ofNullable(findTerritoryName(p, terrPolygons, null));
  }

  /**
   * Finds a land territory name or some sea zone name where the point is contained in according to
   * the territory name -> polygons map. If no land or sea territory has been found a default name
   * is returned.
   *
   * @param p A point on the map.
   * @param terrPolygons a map territory name -> polygons
   * @param defaultTerrName Default territory name that gets returns if nothing was found.
   * @return found territory name of defaultTerrName
   */
  public static String findTerritoryName(
      final Point p, final Map<String, List<Polygon>> terrPolygons, final String defaultTerrName) {
    String lastWaterTerrName = defaultTerrName;
    // try to find a land territory.
    // sea zones often surround a land territory
    for (final String terrName : terrPolygons.keySet()) {
      final Collection<Polygon> polygons = terrPolygons.get(terrName);
      for (final Polygon poly : polygons) {
        if (poly.contains(p)) {
          if (isTerritoryNameIndicatingWater(terrName)) {
            lastWaterTerrName = terrName;
          } else {
            return terrName;
          }
        } // if p is contained
      } // polygons collection loop
    } // terrPolygons map loop
    return lastWaterTerrName;
  }

  /**
   * Checks whether name indicates water or not (meaning name starts or ends with default text).
   *
   * @param territoryName - territory name
   * @return true if yes, false otherwise
   */
  public static boolean isTerritoryNameIndicatingWater(final String territoryName) {
    return territoryName.endsWith(TERRITORY_SEA_ZONE_INFIX)
        || territoryName.startsWith(TERRITORY_SEA_ZONE_INFIX);
  }
}
