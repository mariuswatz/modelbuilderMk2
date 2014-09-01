## ModelbuilderMk2
ModelbuilderMk2 (previously Modelbuilder) is a computational geometry library for Processing. Its focus is to facilitate parametric and generative modeling of geometry, while minimizng complex math and repetitive code where possible. The library is based on a set of simplified data structures with corresponding workflows:

UVertexList represents paths and edges, comprised of UVertex objects. UGeo stores mesh data and automates common mesh-generating operations (quad strips, triangle fans), providing tools to manipulate meshes as unified entities. Additional tools include mesh selection and traversal, export to STL and various time-saving hacks.

**ModelbuilderMk2 prioritizes flexibility and ease of development** over optimal performance, but is still efficient enough to support realtime uses. Its primary design objective is to lower the threshold for non-experts wanting to experiment with computational geometry, without requiring an in-depth understanding of the data structures or math involved.

Borrowing an old concept recently popularized by JavaScript, ModelbuilderMk2 uses operator chaining wherever possible, which makes code more compact and simplifies geometry manipulation. Admittedly, operator chaining can make code harder to read, but if combined with code auto-completion (PDE-X, Eclipse) it greatly simplifies development.

------------------------------

### ModelbuilderMk2 and ITP NYU
**ModelbuilderMk2 was created during my Fall 2013 research residency at NYU ITP,** coinciding with my teaching a class on [Parametric Design and Digital Fabrication](http://workshop.evolutionzone.com/itp-2013-parametric-design-for-digital-fabrication/). ModelbuilderMk2 ended up being the major focus of my residency, giving me the luxury of time to rewrite the library from scratch while adding features I discovered a need for in class.

ModelbuilderMk2 also became a primary tool for teaching principles of parametric form in terms of low-level computational logic, providing an understanding of how geometry is constructed vertex-by-vertex as well as how simple generative principles can be injected into every aspect of the form-generating process to manipulate the outcome.

[Code & Form blog](http://workshop.evolutionzone.com/) / [Marius Watz](http://mariuswatz.com)

------------------------------

### Updates

#### 31 August, 2014 (build 0158)

- **Bug fix**: UGeo.removeDupl() bug, as observed in UGeoGroup01 example pre-0158.
- **unlekker.data** keeps evolving, but should be considered unstable and likely to change.

#### August, 2014

Some good Modelbuilder news:


- **New: unlekker.data** is an extension to ModelbuilderMk2 that deals with typical real-world data scenarios, from parsing, converting and preparing data for visualization. Similarly to how ModelbuilderMk2 uses UVertex and UVertexList to provide a workflow for creating mesh geometry, UDataPoint and UDataList represent data both in atomic and aggregated forms.

	UDataPoint represents a single data point and stores data fields as a map of key-value pairs. This means that UDataPoint can store any kind of data, from primitive strings and numbers to object instances. The matching UDataList class allows you to collect and manipulate data sets of UDataPoint, conveniently automating tasks like calculating min/max/median/average bounds on time series data or extracting a list of all values for a given field.

	Take a look at the examples folder "12-data" for a look at the data classes. Please note that this is my first attempt at this code, and as such may be prone to bugs or logical inconsistencies. I'm hoping this part of library will be useful for teaching, but it's got a way to go yet.


- **New: unlekker.mb2.geo.UTileRender** adds tile rendering to ModelbuilderMk2, a popular feature that makes it possible to produce high-res output from OpenGL sketches by breaking the image into tiles. See "10-advanced/UTileRendering" example for a demo.

	The most recent UTileRender registers "pre" and "post" methods with PApplet to take care of the rendering tasks, requiring a minimum of code to get working. Hopefully this automated approach works in all typical cases, let me know if you find a case where it doesn't.

- **New: unlekker.mb2.UTargaProgressive** is a progressive Targa file encoder,
the code for which was contributed by Dave Bollinger back in the day. Using PImage to store and encode a high res image has limitations in terms of memory usage, typically limiting resolutions to 8000x8000 or less.

	UTargaProgressive is a perfect fix, as it encodes the image in row-sized chunks and flushes the data to disk as needed instead of holding the whole image in memory. If you pass a filename to UTileRender with a ".tga" extension UTargaProgressive will automatically be used.

- **Fixed:** UVertexList.copy() had a bug related to UVertexLists that had been closed with the UVertexList.close() command, with vertices going missing due to being de-duplicated.

#### December 08, 2013

New:

- UGeo.getQ(), UGeoGroup.getQ(): Returns UQuad face data from UGeoGroups of type QUADS or QUAD_STRIP. UQuad extends
UFace, and most standard functions inherited from UFace should work as is. I haven't had a chance to look over the code in detail, so there might be some glitches.
- UGeoGenerator.geodesicSphere(): Creates geodesic sphere by subdividing an icosahedron to a specified level.
- geodesicSphere prototypes: Some standard mesh types are now stored internally for reuse, for instance spheres and geodesics. Pre-generated platonic solids are also included. 
- The new methods related to prototypes are: UGeoGenerator.getPrototype(), UGeoGenerator.listPrototypes(), UGeoGenerator.addProtoType()

Fixes:

- Bug in ASCII STL import
- Bugs in UGeoGenerator (scale() was used instead of scaleToDim())

#### December 08

New:

- Added package: unlekker.mb2.externals. This will contain utility classes for interfacing ModelbuilderMk2 with other libraries, converting data structures as needed and giving access to useful tools not provided by ModelbuilderMk2 itself.
- unlekker.mb2.externals.UGeomerative: Converts data structures from the [Geomerative library by Ricard Marxer](https://github.com/rikrd/geomerative/). Geomerative is useful for reading SVG files and creating vertex outlines of the geometry they contain.
- unlekker.mb2.externals.UPoly2Tri: Interface to [Poly2Tri-java by Wu Liang](http://sites-final.uclouvain.be/mema/Poly2Tri/), making it possible to triangulate both simple (single-contour) and compound (contour with holes) polygons into a valid UGeo mesh. Especially useful for converting RPolygon geometry from Geometry to UGeo.
- UVertexList/UFace.isClockwise: Checks to see if face or vertex list is ordered in clockwise order.


#### December 06

New:

- UEdgeList.getBoundary: Returns edges that are only used by a single face, which means that they lie on the boundary of the mesh.
- UGeoGenerator.extrude: Creates an extruded version of a mesh by copying all faces and translating them along their vertex normals by a given offset. UEdgeList.getBoundary is used to find boundary edges and quads are created to fill in the gaps.
- UGeoGenerator.sphere: Generates sphere mesh.
- UVertexList: reorderToAngle(), reorderToPoint(), shiftOrder() - methods to reorder vertices 
- UVertexList: unclose() - if the last vertex instance is identical to the first, remove it.

Fixes:

- Bug in UVertexList: Transformation methods (scale(), rotX() etc.) did not take into account if a list was closed. Since close() adds another reference to the first UVertex to the end of the list, transformations would be applied twice to that instance. 
- In all honesty, close() is a bit of a kludgy hack. It can cause unexpected behavior in a number of likely scenarios if not taken into account. I recommend using close() as late as possible in the geometry generation process. If subsequent manipulation is necessary, check isClosed() and apply unclose() if needed.
- UVertexList.unclose() has been added to make it easier to deal with this kind of issue. The new reorder/shiftOrder methods use unclose().
- Bug in UFile: nextFile() was broken. Now it isn't.

#### December 02

Good news: I found a way to optimize a big bottleneck in UGeo. By default, UGeo tries to eliminate duplicate vertices, which is a GOOD THING. But all that checking for duplicates becomes a BAD THING for complex meshes, resulting in calls to UVertex.equals() numbering in the hundreds of thousands.

Mesh-building operations like quadstrip(UVertexList vl,UVertexList vl2) and quadstrip(ArrayList<UVertexList> stack) can slow to a crawl for meshes with 1000+ vertices. Build times over a minute for a quadstrip operation is just embarrassing.

The fix:

- Pre-add all new vertices and only initializing UFaces with pre-calculated vertex IDs
- Add UGeo.removeDupl() to give the option to remove duplicates on demand
- Temporarily disable NODUPL for quadstrip(ArrayList<UVertexList> stack), followed by a call to UGeo.removeDupl() to remove duplicate vertices and remap face vertex IDs

In testing this does seem to speed things up quite a bit. Complex mesh building will still take time, but should be much faster. 

**Other news:**

- UGeo.vertexNormals(): Calculate vertex normals (as opposed to face normals) for a mesh. Useful for displacing vertices along their normals, which will be more "natural" than simple random XYZ displacement
- UColor: New class for generating color palettes, using gradients. This is a preliminary version, not extensively tested.
- Added new examples.

#### November 13

**Discovered and fixed:** Nightmare bug in which UFace would calculate arbitray face normals. E(mbarrassing: I sorted the vertex ID array.) All recent builds are likely to contain the bug, so please download the most recent build (any build after Nov 12.)

**Added:** Early (not heavily tested) versions of new classes I think will prove helpful in the future: 

- UGeoGroup: Records UGeo mesh operations like quadstrip and trianglefan to facilitate subsequent manipulation.
- UGeoSelector: Tool to select faces in a UGeo mesh, including faces connected to a given face. Provides tools to draw a preview of the selection and to "grow" the current selection to include adjacent faces.
- UEdgeList / UEdge: Tools to represent the unique edges in a UGeo mesh. All UEdges store a record of faces connected to the edge, making it possible to find the connected faces for any given face.

I have yet to create Processing examples showing this functionality, but curious souls can look in "src-modelbuilderMk2-Test" to see test code.

#### November 12

Added ITP-Workshops, which will contain code from workshops I teach at ITP. Code from this weekend's "Sound as Data" workshop can be found there.

I've made several minor and incremental changes to Modelbuilder, I added a few examples showing some of the nicer ones. I now use Ant to build the library, an archive of builds can be found in the "export" folder.


#### October 30

Added source folder "src-itp-sketches". It contains miscellaneous code demos and ModelbuilderMk2 tutorials demonstrated in the Parametric Design class at ITP. I've added some examples to the 

#### October 20

Plenty of incremental updates and fixes, notably the new UHeading class which supports aligning geometry and vertex lists to heading vectors given by two vertices. That piece of code is based on the [Apache Commons Mathematics Library](http://commons.apache.org/proper/commons-math),
specifically the org.apache.commons.math.geometry package. 

STL import has been added, see UGeoIO.readSTL().


#### Sept 28

ModelbuilderMk2: The core functionality of the library is now in place, offering a full replacement for the old Modelbuilder's geometry workflow. The new library design offers many subtle improvements along with some totally new tools. 

Some elements from the old code base (for instance USimpleGUI) are still missing. Given that many functions were added ad hoc and likely only used by myself, I will only re-implement  missing classes that seem important to common uses for the library.

News:

- unlekker.mb2.geo.UNav3D has been re-implemented. If UNav3D is instantiated after UMB.setPApplet() has been called it will automatically register with that PApplet instance to receive events.
- unlekker.mb2.util.UBase (the base class that is extended by most of the core classes) has been renamed to unlekker.mb2.util.UMB for brevity and clarity. (For a second it was called UMbMk2, but that proved too hard to pronounce in class....)
- unlekker.mb2.geo.UTriangulate now offers triangulation of vertex sets that represent 2.5D topologies. It is based on Florian Jenett's code from [http://wiki.processing.org/w/Triangulation](http://wiki.processing.org/w/Triangulation), but it hasn't been extensively tested and appears to have some peculiarities.
- Added UGeo.triangulation(), adds faces produced by triangulation of a UVertexList. As with UTriangulate, this code has not been extensively tested.
- Added UVertexList.point(t), returns an interpolated vertex at position "t" along the vertex list. Ideally this should be done by accounting for actual length of the list, currently the calculation is done using the number of vertices so that t=0.5 of a list with 33 vertices will give an interpolation t=0.5 between the vertices at positions 16 and 17 (t*0.33= position 16 with 0.5 remainder)
- Added UVertexList.resample(n), creates a resampled version of a UVertexList with "n" number of vertices. For now this function uses UVertexList.point(), which is not an ideal solution since it is based only on vertex indices rather than the actual vector length of the list.
- Added UVertexList.copyNoDupl(), returns a copy of the given list with duplicate vertices removed.

#### Sept 7

ModelbuilderMk2: Added STL export and file utilities (UFile). The current code works with 2.0 and 1.5.1, but I'm not doing a packaged Processing-ready release just yet. If you're working in Eclipse or similar IDE you can download the code and plug it in.

#### Sept 6

Added first version of ModelbuilderMk2, which is a complete rewrite of Modelbuilder. Currently implemented (but incomplete): UVertex, UVertexLists, UGeo - basic features for mesh creation. STL output is not included yet.

#### Sept 5

Added ProcessingData library, which is a hack to make the Processing 2.0 Data API available for use in Processing 1.5.1.

#### Aug 30 

Posted code for Modelbuilder-0020, which is compatible with Processing 1.5.1. We will eventually migrate to Processing 2.0, but for now 1.5.1 is a more stable platform and many of the Modelbuilder examples have not been updated to be compatible. This build uses ControlP5 0.5.4 by Andreas Schlegel, which can be [downloaded from his repository](https://code.google.com/p/controlp5/downloads/detail?name=controlP5_0.5.4.zip&can=2&q=).

Modelbuilder is in bad need of restructuring. It currently suffers from an abundanc of ad hoc hackery and some poor design decisions, hardly surprising given that is my first serious attempt at writing a comprehensive geometry library. I plan to rewrite it from scratch as part of teaching this class.

