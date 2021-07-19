import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('FileDocument e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/file-documents*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('file-document');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load FileDocuments', () => {
    cy.intercept('GET', '/api/file-documents*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('file-document');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('FileDocument').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details FileDocument page', () => {
    cy.intercept('GET', '/api/file-documents*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('file-document');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('fileDocument');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create FileDocument page', () => {
    cy.intercept('GET', '/api/file-documents*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('file-document');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('FileDocument');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit FileDocument page', () => {
    cy.intercept('GET', '/api/file-documents*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('file-document');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('FileDocument');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of FileDocument', () => {
    cy.intercept('GET', '/api/file-documents*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('file-document');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('FileDocument');

    cy.get(`[data-cy="fileName"]`).type('کاربردی بختیاری بدون', { force: true }).invoke('val').should('match', new RegExp('کاربردی بختیاری بدون'));


    cy.setFieldImageAsBytesOfEntity('fileContent', 'integration-test.png', 'image/png');


    cy.get(`[data-cy="filePath"]`).type('بورکینافاسو باغ Cambridgeshire', { force: true }).invoke('val').should('match', new RegExp('بورکینافاسو باغ Cambridgeshire'));


    cy.get(`[data-cy="description"]`).type('صورتی ماشین بحرین', { force: true }).invoke('val').should('match', new RegExp('صورتی ماشین بحرین'));

    cy.setFieldSelectToLastOfEntity('party');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/file-documents*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('file-document');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of FileDocument', () => {
    cy.intercept('GET', '/api/file-documents*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/file-documents/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('file-document');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('fileDocument').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/file-documents*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('file-document');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
