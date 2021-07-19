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

describe('FoodType e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/food-types*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('food-type');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load FoodTypes', () => {
    cy.intercept('GET', '/api/food-types*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('food-type');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('FoodType').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details FoodType page', () => {
    cy.intercept('GET', '/api/food-types*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('food-type');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('foodType');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create FoodType page', () => {
    cy.intercept('GET', '/api/food-types*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('food-type');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('FoodType');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit FoodType page', () => {
    cy.intercept('GET', '/api/food-types*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('food-type');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('FoodType');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of FoodType', () => {
    cy.intercept('GET', '/api/food-types*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('food-type');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('FoodType');

    cy.get(`[data-cy="title"]`).type('پذیرفتن', { force: true }).invoke('val').should('match', new RegExp('پذیرفتن'));


    cy.get(`[data-cy="typeCode"]`).type('جمهوری پروژه', { force: true }).invoke('val').should('match', new RegExp('جمهوری پروژه'));


    cy.get(`[data-cy="description"]`).type('بازی گرانیتی quantifying', { force: true }).invoke('val').should('match', new RegExp('بازی گرانیتی quantifying'));

    cy.setFieldSelectToLastOfEntity('party');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/food-types*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('food-type');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of FoodType', () => {
    cy.intercept('GET', '/api/food-types*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/food-types/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('food-type');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('foodType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/food-types*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('food-type');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
