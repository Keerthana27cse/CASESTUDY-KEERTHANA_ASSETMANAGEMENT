import { jsPDF } from 'jspdf';

const PDF = (form) => {
  const doc = new jsPDF();

  // Header styling
  doc.setFontSize(18);
  doc.setTextColor(33, 150, 243); // Blue
  doc.text('Asset Request Form', 20, 20);

  // Line separator
  doc.setLineWidth(0.5);
  doc.line(20, 25, 190, 25);

  // Section label styles
  doc.setFontSize(12);
  doc.setTextColor(0, 0, 0); // Black
  const leftMargin = 20;
  let top = 35;
  const lineSpacing = 10;

  const addField = (label, value) => {
    doc.setFont('helvetica', 'bold');
    doc.text(`${label}:`, leftMargin, top);
    doc.setFont('helvetica', 'normal');
    doc.text(value || '-', leftMargin + 50, top);
    top += lineSpacing;
  };

  addField('Asset Name', form.assetName);
  addField('Category Name', form.categoryName);
  addField('Description', form.description);
  addField('Reason for Request', form.requestReason);
  addField('Shipping Address', form.fullAddress);
  addField('Zip Code', form.zipCode);
  addField('Phone Number', form.phone);

  return doc;
};

export default PDF;
