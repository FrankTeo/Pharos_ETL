create or replace package rmnode is

  PROCEDURE removenode(v_objectid IN VARCHAR2);
  PROCEDURE removeproposal(v_objectid IN VARCHAR2);
  PROCEDURE removequotation(v_objectid IN VARCHAR2);

end rmnode;
