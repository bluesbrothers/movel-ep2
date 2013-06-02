include names.mk

GPS_PDF_PATH=$(GPSDIR)/$(GPSPDF)
RSS_PDF_PATH=$(RSSDIR)/$(RSSPDF)
BANDEX_PDF_PATH=$(BANDEXDIR)/$(BANDEXPDF)

DOC_ENTREGA_DIR=Documentation

alo:
	echo "XD"

pdfgps:
	cd $(GPSDIR); make pdf

pdfrss:
	cd $(RSSDIR); make pdf

pdfbandex:
	cd $(BANDEXDIR); make pdf

pdfrelatorio:
	cd $(DOCDIR); make

$(DOC_ENTREGA_DIR):
	if [ ! -d $(DOC_ENTREGA_DIR) ]; then mkdir $(DOC_ENTREGA_DIR); fi

pdf: $(DOC_ENTREGA_DIR) pdfgps pdfrss pdfbandex pdfrelatorio
	cp $(GPS_PDF_PATH) $(DOC_ENTREGA_DIR)
	cp $(RSS_PDF_PATH) $(DOC_ENTREGA_DIR)
	cp $(BANDEX_PDF_PATH) $(DOC_ENTREGA_DIR)
	cp $(DOCDIR)/relatorio.pdf $(DOC_ENTREGA_DIR)

entrega: pdf
	if [ -d diogo-e-fernando ]; then rm -r diogo-e-fernando; fi
	mkdir diogo-e-fernando
	cp README diogo-e-fernando
	cp -r Documentation diogo-e-fernando
	cp -r bandex/eclipse-project diogo-e-fernando/bandex-project
	cp -r rss/eclipse-project diogo-e-fernando/rss-project
	cp -r gps/eclipse-project diogo-e-fernando/busp-project
	rm -rf diogo-e-fernando/*-project/gen
	mkdir diogo-e-fernando/apks
	cp diogo-e-fernando/*-project/bin/*.apk diogo-e-fernando/apks
	rm -rf diogo-e-fernando/*-project/bin
	tar -czvf diogo-e-fernando.tar.gz diogo-e-fernando

.PHONY: clean
clean:
	rm -rf $(DOC_ENTREGA_DIR)
	cd $(GPSDIR); make clean
	cd $(RSSDIR); make clean
	cd $(BANDEXDIR); make clean
	cd $(DOCDIR); make clean
