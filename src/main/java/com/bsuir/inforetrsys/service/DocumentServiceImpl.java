package com.bsuir.inforetrsys.service;

import com.bsuir.inforetrsys.api.service.DocumentService;
import com.bsuir.inforetrsys.entity.TextDocument;
import com.bsuir.inforetrsys.repository.impl.DocumentRepository;
import com.bsuir.inforetrsys.repository.specification.document.AllDocumentsSpecification;
import com.bsuir.inforetrsys.repository.specification.document.DocumentByIdSpecification;
import com.bsuir.inforetrsys.repository.specification.document.LastDocumentSpecification;
import com.epam.cafe.api.repository.Repository;
import com.epam.cafe.repository.RepositoryException;
import com.epam.cafe.service.ServiceException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DocumentServiceImpl implements DocumentService {
    private static final int FIRST = 0;

    Repository<TextDocument> repository = new DocumentRepository();

    @Override
    public Set<String> getAllIndexedDocumentPaths() throws ServiceException {
        List<TextDocument> documents;
        try {
            documents = repository.query(new AllDocumentsSpecification());
        } catch (RepositoryException e) {
            throw new ServiceException("Problems with getting indexed documents paths", e);
        }
        return documents.stream()
                .map(TextDocument::getFilePath)
                .collect(Collectors.toSet());
    }

    @Override
    public int getDocumentsNumber() throws ServiceException {
        List<TextDocument> documents;
        try {
            documents = repository.query(new AllDocumentsSpecification());
        } catch (RepositoryException e) {
            throw new ServiceException("Problems with getting documents number", e);
        }
        return documents.size();
    }

    @Override
    public void saveDocument(TextDocument document) throws ServiceException {
        try {
            repository.save(document);
        } catch (RepositoryException e) {
            throw new ServiceException("Problems with saving document: " + document, e);
        }
    }

    @Override
    public int getLastDocumentId() throws ServiceException {
        int documentId;
        try {
            List<TextDocument> documents = repository.query(new LastDocumentSpecification());
            TextDocument document = documents.get(FIRST);
            documentId = document.getId();
        } catch (RepositoryException e) {
            throw new ServiceException("Problems with getting last document id", e);
        }
        return documentId;
    }

    @Override
    public TextDocument getDocumentById(int id) throws ServiceException {
        TextDocument document;
        try {
            List<TextDocument> documents = repository.query(new DocumentByIdSpecification(id));
            document = documents.get(FIRST);
        } catch (RepositoryException e) {
            throw new ServiceException("Problems with getting document with id: '" + id + "'", e);
        }
        return document;
    }
}
