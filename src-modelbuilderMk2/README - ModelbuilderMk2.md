

ModelbuilderMk2 - Geometry library for Processing
====================

Marius Watz, 2013 - [workshop.evolutionzone.com](http://workshop.evolutionzone.com/)

Preliminary alpha version of ModelbuilderMk2, a complete rewrite of the Modelbuilder geometry library for Processing. See [Modelbuilder-0020](https://github.com/mariuswatz/modelbuilder) for the previous version.

Modelbuilder was created to simplify the creation of parametric geometry while minimizing the repetitive tasks of computational geometry. It defines a workflow based on vertex lists, which can be copied and manipulated to produce meshes through familiar methods like triangle fans and quadstrips. The library is biased towards digital fabrication, but can also be useful for realtime graphics.

Modelbuilder has proven its usefulness as a teaching tool as well as an engine for real-world projects. But as a first attempt at writing a comprehensive geometry library, it currently suffers from certain design. Worse, it's full of ad-hoc hackery and semi-broken code (fortunately largely undocumented.)

### ModelbuilderMk2 

ModelbuilderMk2 is a clean slate rewrite, taking the best features of the original library but paying more attention to consistency and clarity. It uses ArrayLists rather than arrays throughout, taking advantage of easy iteration and object insertion/removal. 

I am writing ModelbuilderMk2 as part of my [Fall 2013 ITP NYU class on Parametric Design and Digital Fabrication](http://workshop.evolutionzone.com/itp-2013-parametric-design-for-digital-fabrication/). Input and bug reports are welcome.

